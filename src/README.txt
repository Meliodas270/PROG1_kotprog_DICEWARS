Hallo!

Gondolom azért olvasod ezt, mert el szeretnéd indítani a kötelező programot, amit tesztelned kell.
Ha nem ilyen indokaid vannak, akkor is érdemes lehet elolvasnod ezt, mert ha nem teszed, akkor érdekes meglepetésekre bukkanhatsz a program futása közben. :D

---
Fontos, hogy 11 vagy nagyobb JavaJDK-d legyen telepítve a gépedre!

---
Nos, először is, ha parancssorosan szeretnéd tesztelni a programot, megkönnyítve a dolgodat Tesztelő, van egy "sources.txt" nevű file, ugyan ebben a mappában. Ha a parancssorba (amin belül ugye ugyan ebben a mappában vagy?) bemásolod, hogy:

javac @sources.txt

akkor ez le fogja neked compile-olni az összes, a játék futásához szükséges .java file-t, így elkészítve a futtatáshoz szükséges .class file-okat. Your welcome!

Mindezek után másold be a parancssorba a következő sort, és ha valami gondod van, nyisd meg a "README2.txt" file-t további segítségért:

java Main

---
Ha IDEA-ban szeretnéd tesztelni a programot, csak importold az egész mappát és indítsd el a Main.java-t!
Elméletben mindent rendesen látnod kell majd!

---
A tesztelés megkönnyítéséhez van egy DevSettings mappa, amiben hasznos változókat találsz, például, hogy minden köröd automatikusan adódjon át, ne kelljen mindig manuálisan átadnod. Ugyan itt meg tudod változtatni a kiíratások után várt időt is.

---
A JAVADOC mappában értelemszerűen megtalálod a program (legtöbb/összes/majdnemminden) metódusának leírását.

---
Sidenote:
A parancssorosan és IDEA-ban készített mentések külön külön mappákban vannak, az egyik nem látja a másikat.


---
Ha elakadnál valamiben CooSpace-dobj egy üzit és segítek!
HFTARS // Kosik Roland Csaba

Jó játékot és tesztelést kívánok!