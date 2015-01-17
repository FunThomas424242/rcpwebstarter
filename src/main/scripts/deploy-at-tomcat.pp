class { 'tomcat': }

tomcat::war { '@WAR_FILE@':
        catalina_base => '/opt/apache-tomcat/tomcat8',
        war_source => "@BASE_DIR@/@WAR_FILE@",
}