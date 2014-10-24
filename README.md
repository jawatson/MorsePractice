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
    $ java -jar java -jar build/jar/MorsePractice.jar

