# RCP Webstarter Project
Ein Webstarter für RCP Anwendungen die irgendwo im Netz als zip File abliegen und keinen laufenden Servlet Container zum Download benötigen.

# Status
[![Download](https://api.bintray.com/packages/funthomas424242/eclipse-features/rcpwebstarter/images/download.svg)](https://bintray.com/funthomas424242/eclipse-features/rcpwebstarter/_latestVersion)

Letzter Shapshot:
[![Build Status](https://travis-ci.org/FunThomas424242/rcpwebstarter.svg?branch=master)](https://travis-ci.org/FunThomas424242/rcpwebstarter)

Das Projekt basiert auf den Arbeiten folgender Projekte:
* http://sourceforge.net/projects/webrcp/
* https://github.com/w11k/webrcp
* https://github.com/FunThomas424242/webrcp.example


# Benutzung
Der Webstarter ist selbst signiert. Das hat zur Folge, dass zum einen das Zertifikat in 3 Monaten ablaufen wird als auch das man für Webstart
eine Ausnahme für den Download URL aufnehmen muss. Beides ist unschön aber da ich nur ein einfacher Open Source Entwickler bin und keine Firma
fällt es mir schwer ein Zertifikat zu bekommen (Kostet vermutlich Geld und gilt vermutlich nur für einen URL und letzterer ändert sich bei mir recht
häufig).

Wer ein eigenes Zertifikat besitzt kann die sourcen downloaden und entsprechend seiner Gegebenheiten konfigurieren (build.properties, build.filter).
Alle Anderen verfahren wie hier beschrieben.

Wie auch immer, ein Test wird nur funktionieren wenn eine Ausnahme im webstart control auf dem Reiter Sicherheit eingefügt wird. Das Webstart Control
bitte auf der Kommandozeile mittels *jcontrol* starten und da wie im nachfolgenden Bild gezeigt die Ausnahme hinzufügen.
![Add Security Exception](src/main/docs/AddSecurityException.png)

# Test 
Um das Webstarter Beispiel zu testen kann nach Aufnahme der Security Ausnahme einfach auf die
[rcpwebstarter.jnlp](https://bintray.com/artifact/download/funthomas424242/eclipse-features/rcpwebstarter/1.0.0/rcpwebstarter.jnlp)
geklickt werden.

# Konfiguration für die eigene RCP Anwendung

Alles was zu tun ist sind 3 Schritte:

1. RCP Anwendung erstellen und zum Zip Archiv zusammen packen und irgendwo im Netz ablegen.
   Beim Export der RCP Anwendung aus Eclipse muss das root Verzeichnis *eclipse* genannt werden.

2. Die JNLP Datei vom Starter downloaden und den Wert des Property *jnlp.WebRCP.archives* auf den URL zum Zip Archiv der RCP Anwendung setzen.

3. RCP Zip Archiv und angepasste JNLP Datei irgendwo zusammen im Netz ablegen. 

![JNLP Datei Source](rcpwebstarter.jnlp)
