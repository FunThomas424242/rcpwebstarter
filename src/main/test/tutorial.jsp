<!-- WebRCP-Example 1.0 RC1 (03.12.2013 / 09:30.51) -->




<?xml version="1.0" encoding="ISO-8859-1"?>
<jnlp
	spec="1.0+"
	codebase="http://localhost:8080/webrcp-example"
	href="/webrcp-example/tutorial.jsp">
	<application-desc></application-desc>

	<information>
		<title>WebRCP-Example</title>
		<vendor>Example Inc</vendor>
		<description>This is a webrcp example</description>
		<homepage href="http://localhost:8080/webrcp-example/index.html"/>
		<icon href="icon.jpeg"/>
		<offline-allowed/>
	</information>

	<security>
		<all-permissions/>
	</security>

  <resources>
  		<j2se version="1.5+" />
		<jar href="webrcp.jar" />
 		<property name="jnlp.WebRCP.appName" value="WebRCP-Example"/>
	   	<property name="jnlp.WebRCP.appVersion" value="1.0.0"/>
	   	<property name="jnlp.WebRCP.launchProduct" value="rcp.example.product"/>
	    <property name="jnlp.WebRCP.archives" value="rcp.example_1.0.0"/>
	   	<property name="jnlp.WebRCP.sysArchives" value=""/>
	   	<property name="jnlp.WebRCP.executable" value="eclipse"/>
	   	<property name="jnlp.WebRCP.singleInstance" value="false"/>
        <property name="jnlp.WebRCP.launcherjar" value="eclipse/plugins/org.eclipse.equinox.launcher_1.3.0.v20130327-1440.jar"/>
        <property name="jnlp.WebRCP.launcherclass" value="org.eclipse.equinox.launcher.Main"/>
		
		<!-- more custom properties -->
		<!-- will be loaded and set as System Property without the jnlp.custom part -->
  		<property name="jnlp.custom.java.security.auth.login.config" value="http://@SERVER@:@WEBPORT@/login.conf"/>
		<property name="jnlp.custom.server" value="@SERVER@:@JNPPORT@"/>
    
  </resources>
</jnlp>

