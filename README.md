# rcpwebstarter Project
Ein Webstarter für RCP Anwendungen die irgendwo im Netz als zip File abliegen und keinen laufenden Servlet Container zum Download benötigen.

#Status
[![Download](https://api.bintray.com/packages/funthomas424242/eclipse-features/rcpwebstarter/images/download.svg)](https://bintray.com/funthomas424242/eclipse-features/rcpwebstarter/_latestVersion)

Letzter Shapshot:
[![Build Status](https://travis-ci.org/FunThomas424242/rcpwebstarter.svg?branch=master)](https://travis-ci.org/FunThomas424242/rcpwebstarter)


#Benutzung
Der Webstarter ist selbst signiert. Das hat zur Folge, dass zum einen das Zertifikat in 3 Monaten ablaufen wird als auch das man für Webstart
eine Ausnahme für den Download URL aufnehmen muss. Beides ist unschön aber da ich nur ein einfacher Open Source Entwickler bin und keine Firma
fällt es mir schwer ein Zertifikat zu bekommen (Kostet vermutlich Geld und gilt vermutlich nur für einen URL und letzterer ändert sich bei mir recht
häufig).

Wie auch immer, ein Test wird nur funktionieren wenn eine Ausnahme im webstart control auf dem Reiter Sicherheit eingefügt wird. Das Webstart Contro
bitte auf der Kommandozeile mittels *jcontrol* starten und da wie im nachfolgenden Bild gezeigt die Ausnahme hinzufügen.
![Add Security Exception](src/main/docs/AddSecurityException.png)

#Test 
Um das Webstarter Beispiel zu testen kann nach Aufnahme der Security Ausnahme einfach auf die
[rcpwebstarter.jnlp](https://bintray.com/funthomas424242/eclipse-features/rcpwebstarter/_latestVersion/rcpwebstarter.jnlp)
geklickt werden.



