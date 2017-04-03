<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns="http://www.w3.org/1999/xhtml">

    <xsl:output method="xml" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
              doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" encoding="UTF-8" indent="yes"/>

    <xsl:key name="byProcessName" match="Database/Logs/AppLogs[@processName]" use="@processName" />

    <xsl:template match="Database">
      <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
          <title>Analyze of debug database</title>
          <link rel="stylesheet" href="htmlOutput.css" type="text/css" />
        </head>
        <body>
            <h1>Statistics about analyzed database</h1>
            <xsl:call-template name="checkDatabase"/>
        </body>
      </html>
    </xsl:template>
  
    <xsl:template name="checkDatabase">
      <xsl:choose>
        <xsl:when test="FunctionStats/@count &gt; 0">
          <div class="details">
           <h2>Database summary</h2>
           <li style="color:red;font-size:22px;">Database contains errors or criticals.</li>
          </div>
          <div class="details">
            <xsl:call-template name="functionStats"/>
          </div>
          <div class="details">
            <xsl:call-template name="processStats"/>
          </div>
          <xsl:call-template name="logs"/>
        </xsl:when>
        <xsl:otherwise>
          <div class="details">
            <h2>Database summary</h2>
            <li style="color:green;font-size:22px;">No errors and criticals in database (in selected process). Statistics of logs for each process are below.</li>
          </div>
          <xsl:call-template name="logs"/>
        </xsl:otherwise>
      </xsl:choose>      
    </xsl:template>
    
    <xsl:template match="Logs" name="logs">
      <h1>Logs grouped by process name</h1>
      <xsl:apply-templates mode="appTemplate" select="Logs"/>
    </xsl:template>
    
    <xsl:template match="FunctionStats" name="functionStats">
      <h2>Statistic about functions which caused errors</h2>
      <table border="1">
        <tr bgcolor="#1e90ff">
          <th>Function name</th>
          <th>Errors count</th>
          <th>Criticals count</th>
        </tr>
        <xsl:apply-templates mode="specificStatsFunction" select="FunctionStats"/>
      </table>
    </xsl:template>
  
    <xsl:template match="ProcessStats" name="processStats">
      <h2>Statistic about processes which caused errors</h2>
      <table border="1">
        <tr bgcolor="#1e90ff">
          <th>Process name</th>
          <th>Errors count</th>
          <th>Criticals count</th>
          <th>Link</th>
        </tr>
        <xsl:apply-templates mode="specificStatsProcess" select="ProcessStats"/>
      </table>
    </xsl:template>
    
    <xsl:template match="Process" mode="specificStatsProcess">
      <xsl:if test="Errors + Criticals &gt; 0">
        <tr>
          <td>
            <xsl:value-of select="Name"/>
          </td>
          <td>
            <xsl:value-of select="Errors"/>
          </td>
          <td>
            <xsl:value-of select="Criticals"/>
          </td>
          <td>
            <a href="#{Name}">Details and logs</a>
          </td>
        </tr>
      </xsl:if>
    </xsl:template>
  
    <xsl:template match="Function" mode="specificStatsFunction">
      <xsl:if test="Errors + Criticals &gt; 0">
        <tr>
          <td>
            <xsl:value-of select="Name"/>
          </td>
          <td>
            <xsl:value-of select="Errors"/>
          </td>
          <td>
            <xsl:value-of select="Criticals"/>
          </td>
        </tr>
      </xsl:if>
    </xsl:template>
  
    <xsl:template match="AppLogs" mode="appTemplate">
      <xsl:if test="not(@processName) or generate-id() = generate-id(key('byProcessName', @processName)[1])">
        <div class="details">
          <a name="{@processName}"/>
          <xsl:call-template name="writeProcessName"></xsl:call-template>
          <details open="true">
            <summary>Process statistics and errors details</summary>
            <xsl:call-template name="appLogStats"/>
            <xsl:choose>
              <xsl:when test="sum(key('byProcessName', @processName)/@critical) + sum(key('byProcessName', @processName)/@error) > 0">
                <div class="logs">
                  <details open="true">
                    <summary>Information about errors and criticals</summary>
                    <xsl:call-template name="createErrorsTable"/>
                  </details>
                </div>
              </xsl:when>
              <xsl:otherwise>
                <h3>No errors and criticals. No specific information about logs associated with this process name.</h3>
              </xsl:otherwise>
            </xsl:choose>
          </details>
        </div>
      </xsl:if>
    </xsl:template>
  
    <xsl:template name="writeProcessName">
      <xsl:choose>
        <xsl:when test="@error + @critical &gt; 0">
          <h2>
            <font color="red">
              <xsl:value-of select="@processName"/>
            </font>
          </h2>
        </xsl:when>
        <xsl:otherwise>
          <h2>
            <font color="black">
              <xsl:value-of select="@processName"/>
            </font>
          </h2>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>
  
    <xsl:template name="appLogStats">
      <table border="1">
        <tr bgcolor="#1e90ff">
          <th>Type</th>
          <th>Level</th>
          <th>Count</th>
        </tr>
        <xsl:call-template name="fillAppLogStats"/>
      </table>
    </xsl:template>
  
    <xsl:template name="fillAppLogStats">
      <tr>
        <td>Critical</td>
        <td>1000</td>
        <td>
          <xsl:value-of select="sum(key('byProcessName', @processName)/@critical)"/>
        </td>
      </tr>
      <tr>
        <td>Error</td>
        <td>800</td>
        <td>
          <xsl:value-of select="sum(key('byProcessName', @processName)/@error)"/>
        </td>
      </tr>
      <tr>
        <td>Warning</td>
        <td>600</td>
        <td>
          <xsl:value-of select="sum(key('byProcessName', @processName)/@warning)"/>
        </td>
      </tr>
      <tr>
        <td>Debug</td>
        <td>400</td>
        <td>
          <xsl:value-of select="sum(key('byProcessName', @processName)/@debug)"/>
        </td>
      </tr>
      <tr>
        <td>Info</td>
        <td>200</td>
        <td>
          <xsl:value-of select="sum(key('byProcessName', @processName)/@info)"/>
        </td>
      </tr>
      <tr>
        <td>Verbose</td>
        <td>100</td>
        <td>
          <xsl:value-of select="sum(key('byProcessName', @processName)/@verbose)"/>
        </td>
      </tr> 
    </xsl:template>
  
    <xsl:template name="createErrorsTable">
      <table border ="1">
        <tr bgcolor="#1e90ff">
          <th>First ID</th>
          <th>Last ID</th>
          <th>Count</th>
          <th>Level</th>
          <th>Type</th>
          <th>Module</th>
          <th>Process ID</th>
          <th>Thread ID</th>
          <th>From date</th>
          <th>To date</th>
          <th>Caused by</th>
        </tr>
        <xsl:call-template name="iterateThroughLogs"/>
      </table>
    </xsl:template>
  
    <xsl:template name="iterateThroughLogs">
      <xsl:for-each select=". | key('byProcessName', processName)">
        <xsl:for-each select="Log">
          <xsl:choose>
            <xsl:when test="level = 800 or level = 1000">
              <xsl:call-template name="fillErrorsTable">
                <xsl:with-param name="color">red</xsl:with-param>
              </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
              <xsl:call-template name="fillErrorsTable">
                <xsl:with-param name="color">black</xsl:with-param>
              </xsl:call-template>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:for-each>
      </xsl:for-each>
    </xsl:template>
  
    <xsl:template name="fillErrorsTable">
      <xsl:param name="color"/>
      <tr>
        <td>
          <font color="{$color}">
            <xsl:value-of select="startID"/>
          </font>
        </td>
        <td>
          <font color="{$color}">
            <xsl:value-of select="endID"/>
          </font>
        </td>
        <td>
          <font color="{$color}">
            <xsl:value-of select="count"/>
          </font>
        </td>
        <td>
          <font color="{$color}">
            <xsl:value-of select="level"/>
          </font>
        </td>
        <td>
          <font color="{$color}">
            <xsl:value-of select="type"/>
          </font>
        </td>
        <td>
          <font color="{$color}">
            <xsl:value-of select="module"/>
          </font>
        </td>
        <td>
          <font color="{$color}">
            <xsl:value-of select="pid"/>
          </font>
        </td>
        <td>
          <font color="{$color}">
            <xsl:value-of select="tid"/>
          </font>
        </td>
        <td>
          <font color="{$color}">
            <xsl:value-of select="startDate"/>
          </font>
        </td>
        <td>
          <font color="{$color}">
            <xsl:value-of select="endDate"/>
          </font>
        </td>
        <td>
          <font color="{$color}">
            <xsl:value-of select="info"/>
          </font>
        </td>
      </tr>
    </xsl:template>
</xsl:stylesheet>

