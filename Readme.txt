Scheduler - Andreas Hofer

Passwort und Email für den Adminbenutzer sind: 'admin@scheduler.com' und '12345'. Alternativ kann man alle Benutzerdaten in der MockDataService.java Klasse im Services Ordner finden.

Eventuell ist es nötig mit den folgenden JAVA_TOOL_OPTIONS zu arbeiten: --add-opens=java.base/java.lang=ALL-UNNAMED, --add-opens=java.base/java.util=ALL-UNNAMED, --add-opens=java.base/java.time=ALL-UNNAMED

Was ich nicht implementiert habe ist die Inkenntnissetzung des Assistentenbenutzers, falls eine Preference nicht wahrgenommen wird. Zusätzlich werden Studenten nicht darüber informiert, wenn eine Einschreibung zu einem Konflikt führt. Da es laut Angabe jedoch möglich sein sollte, passiert es einfach nur ohne Warnung. Dabei fehlt jedoch nur die Interaktion im Interface und im Backend gibt es sehr wohl Überprüfungen ob Kurse kollidieren und es wird ein Objekt mit den ids der Kollisionen statt einem OK gesendet, dieses wird jedoch nicht wahrgenommen.