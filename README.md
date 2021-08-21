### Installation

Note: this is for a Mac with homebrew installed.

I followed this guide:

https://www.flinkinpractice.com/2019/07/28/Set-Up-Flink-on-Mac-OS-X/

When I ran `java -version`, a popup took me to this website to download java:

https://www.oracle.com/java/technologies/javase-jdk16-downloads.html

After I downloaded the JDK (the Java Development Kit) I got the following when
I ran `java -version` again:

```
java version "16.0.2" 2021-07-20
Java(TM) SE Runtime Environment (build 16.0.2+7-67)
Java HotSpot(TM) 64-Bit Server VM (build 16.0.2+7-67, mixed mode, sharing)
```

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
