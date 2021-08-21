This is just some notes on the hot mess I got myself into and some links that
helped get me out of it.

### Don't do the stuff in this document!  Follow the README instead.

When I ran `java -version`, a popup took me to this website to download java:

https://www.oracle.com/java/technologies/javase-jdk16-downloads.html

After I downloaded the JDK (the Java Development Kit) I got the following when
I ran `java -version` again:

```
java version "16.0.2" 2021-07-20
Java(TM) SE Runtime Environment (build 16.0.2+7-67)
Java HotSpot(TM) 64-Bit Server VM (build 16.0.2+7-67, mixed mode, sharing)
```

But note!!! Flink does not work with Java 16... only with Java 8 or 11,
according to this stack overflow article:
https://stackoverflow.com/questions/67768612/got-exception-when-running-the-localhost-cluster

So actually, I had to uninstall that, using these instructions that I'm nearly certain will not work...

https://www.java.com/en/download/help/mac_uninstall_java.html

... then install

https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html

Then I clicked download on jdk-8u301-macosx-x64.dmg

Then... sign up for an Oracle account (really?!?!)... I didn't have to do that
for Java 16.  Maybe because this is for an older Java version?  Who knows?

Ok, I installed Java 8.

Let's see...

```
java -version
java version "16.0.2" 2021-07-20
Java(TM) SE Runtime Environment (build 16.0.2+7-67)
Java HotSpot(TM) 64-Bit Server VM (build 16.0.2+7-67, mixed mode, sharing)
```

fu.., but also totally unsurprising.  Kinda regretting not doing this in Docker compose now.
I *knew* this wouldn't work easily, but I was lulled into a false sense of confidence.  This
is where software hate comes from.

Oh, ok, cool, there's a "Java Uninstall Tool" at https://www.java.com/en/download/uninstalltool.jsp

Let's try that!

Ok, when I ran it, it found JRE 1.8.3.  Which JDK is that associated with?  Who knows?!?!

```
java -version
java version "16.0.2" 2021-07-20
Java(TM) SE Runtime Environment (build 16.0.2+7-67)
Java HotSpot(TM) 64-Bit Server VM (build 16.0.2+7-67, mixed mode, sharing)
```

fu.. but *again* not surprising.  Like I'd expect the Java uninstaller to uninstall Java.  Pshh.  Newb.

Again regretting not doing this in Docker compose.  But, I also hate *running* things in Docker compose.

It's Saturday.  It's beautiful outside, and I'm fucking around with Java versions because... Flink 1.13 apparently doesn't support Java 16.

Ok, so I missed a critical step here: https://stackoverflow.com/questions/51780530/how-do-i-uninstall-the-java-jdk-in-macos

Use homebrew or not?  Apparently that was the guidance a while ago, but now
you'd have to be a fool to do that, because it doesn't install the examples.
And good luck to you if you don't have the examples.  What, you think you'll
just be able to, like, run some script?  You're obviously a newb.  Your script will never work.  The best chance you have is the examples,
so, like skip homebrew.

Oh, wait, you want to run on Amazon EMR?  Well, that's, of course very old, so
back up to 1.12.  There's no chance that that will support any kind of new
Java, so you're even happier off the happy path, sucker.

Next, actually install Flink

```
brew install apache-flink
```

Then check the version

```
flink --version
```

```
Version: 1.13.2, Commit ID: 5f007ff
```

Finally, you might want to either add the directory to your PATH or add a shell
function to make it easy to start flink:

function fli {
  cd /usr/local/Cellar/apache-flink/1.13.2/libexec/bin
  ./start-cluster.sh
  open http://localhost:8081/
}
