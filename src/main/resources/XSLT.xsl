<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns="http://www.w3.org/1999/xhtml">

    <xsl:output method="xml" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
              doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" encoding="UTF-8" indent="yes"/>

    <xsl:key name="byProcessName" match="Processes/AppLogs[@processName]" use="@processName" />

    <xsl:template match="Database">
      <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
          <title>Analyza debug databazy</title>
          <link rel="stylesheet" href="htmlOutput.css" type="text/css" />
        </head>
        <body>
          <xsl:call-template name="logs"/>
        </body>
      </html>
    </xsl:template>
    
    <xsl:template match="Logs" name="logs">
      <h1>Logs grouped by process name</h1>
      <xsl:call-template name="functionStats"/>
      <xsl:call-template name="processStats"/>
    </xsl:template>
    
    <xsl:template match="FunctionStats" name="functionStats">
      <table border="1" bgcolor="#1e90ff">
        <tr>
          <th>Function name</th>
          <th>Errors count</th>
          <th>Criticals count</th>
        </tr>
        <xsl:apply-templates mode="specificStats" select="FunctionStats"/>
      </table>
    </xsl:template>
  
    <xsl:template match="ProcessStats" name="processStats">
      <table border="1" bgcolor="#1e90ff">
        <tr>
          <th>Process name</th>
          <th>Errors count</th>
          <th>Criticals count</th>
        </tr>
        <xsl:apply-templates mode="specificStats" select="ProcessStats"/>
      </table>
    </xsl:template>
    
    <xsl:template match="Function | Process" mode="specificStats">
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
    </xsl:template>

    <!--
    <xsl:template match="AppLogs" name="appTemplate">
        <xsl:if test="not(@processName) or generate-id() = generate-id(key('byProcessName', @processName)[1])">
     	    <div class="details">  
            <h2>
                <xsl:value-of select="@processName"/>
            </h2>
            <details>
              <summary>Process statistics and errors details</summary>
              <table border="1">
                <tr bgcolor="#1e90ff">
                  <th>Type</th>
                  <th>Level</th>
                  <th>Count</th>
                </tr>
                <tr><td>Critical</td><td>1000</td><td><xsl:value-of select="sum(key('byProcessName', @processName)/@critical)"/></td></tr>
                <tr><td>Error</td><td>800</td><td><xsl:value-of select="sum(key('byProcessName', @processName)/@error)"/></td></tr>
                <tr><td>Warning</td><td>600</td><td><xsl:value-of select="sum(key('byProcessName', @processName)/@warning)"/></td></tr>
                <tr><td>Debug</td><td>400</td><td><xsl:value-of select="sum(key('byProcessName', @processName)/@debug)"/></td></tr>
                <tr><td>Info</td><td>200</td><td ><xsl:value-of select="sum(key('byProcessName', @processName)/@info)"/></td></tr>
                <tr><td>Verbose</td><td>100</td><td><xsl:value-of select="sum(key('byProcessName', @processName)/@verbose)"/></td></tr>                 
              </table>
              <xsl:choose>
                <xsl:when test="sum(key('byProcessName', @processName)/@critical) + sum(key('byProcessName', @processName)/@error) > 0">
                  <p/>
                  <div class="logs">
                  <details>
                  <summary>Information about errors and criticals</summary>
                  <table border ="1">
                    <tr bgcolor="#1e90ff">
                      <th>Process ID</th>
                      <th>First ID</th>
                      <th>Last ID</th>
                      <th>Count</th>
                      <th>Level</th>
                      <th>Type</th>
                      <th>Thread ID</th>
                      <th>Caused by</th>
                    </tr>
                    <xsl:for-each select=". | key('byProcessName', @processName)">
                      <xsl:for-each select="Log">
                        <xsl:choose>
                        <xsl:when test="@level = 800 or @level = 1000">
                          <tr>
                            <td><font color="red"><xsl:value-of select="@pid"/></font></td>
                            <td><font color="red"><xsl:value-of select="@startID"/></font></td>
                            <td><font color="red"><xsl:value-of select="@endID"/></font></td>
                            <td><font color="red"><xsl:value-of select="@count"/></font></td>
                            <td><font color="red"><xsl:value-of select="@level"/></font></td>
                            <td><font color="red"><xsl:value-of select="@type"/></font></td>
                            <td><font color="red"><xsl:value-of select="@tid"/></font></td>
                            <td><font color="red"><xsl:value-of select="text()"/></font></td>
                          </tr>
                        </xsl:when>
                        <xsl:otherwise>
                            <tr>
                            <td><xsl:value-of select="@pid"/></td>
                            <td><xsl:value-of select="@startID"/></td>
                            <td><xsl:value-of select="@endID"/></td>
                            <td><xsl:value-of select="@count"/></td>
                            <td><xsl:value-of select="@level"/></td>
                            <td><xsl:value-of select="@type"/></td>
                            <td><xsl:value-of select="@tid"/></td>
                            <td><xsl:value-of select="text()"/></td>
                          </tr>
                        </xsl:otherwise>
                        </xsl:choose>
                      </xsl:for-each>
                    </xsl:for-each>
                  </table>
                  </details>
                </div>
                </xsl:when>
                <xsl:otherwise>
                  <h4>No errors and criticals. No specific information about logs associated with this process name.</h4>
                </xsl:otherwise>
            </xsl:choose>
            </details>
            </div>
        </xsl:if>
    </xsl:template>
    -->
</xsl:stylesheet>

