MorsePractice
=============

An updated version of Martin Minow's Morse Practice applet/application.

This is an update of Martin Minow's excellent Morse Practice application, based on the source code available at Martin's (posthumously maintained) website [https://www.merrymeet.com/minow/MorsePractice/MorsePractice.html](https://www.merrymeet.com/minow/MorsePractice/MorsePractice.html).

The updates include;

* Use of swing widgets throughout
* Various minor amendments to remove compiler warnings (typically caused by the introduction of generics).

Building and Running the Application 
------------------------------------
From the top level directory of the clone;

    $ ant jar
    $ java -jar build/jar/MorsePractice.jar

Ubuntu
-----
Ubuntu users will be able to run the application by setting the file to
executable with the following command and double clicking on the 
.jar file.

    $ chmod +x MorsePractice.jar

Fedora
------
On Fedora, copy the file 'run-jar.desktop' to the local applications 
folder with the following command;

    $ sudo cp run-jar.desktop /usr/local/share/applications

Right-Click on the MorsePractice.jar file and select, 'properties' - 
'Open With'.  From 'Other Applictions' list, select 'run-jar'.

