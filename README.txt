
2018_06_28 ver 1.0

■ 実行方法

>サーバ立ち上げ後
$ java OthelloClient [server IP] [port number] [username]

例:
$ java OthelloClient 127.0.0.1 8888 takashi

■ 特殊なusername
usernameの部分に下記のパラメータを入れて実行するといいことがあるかもしれない。
tnb       :くそ雑魚エンジニアAI
kusoanime :エイサーいはラマスこーい
shacho    :ずっと俺のターン
maccho    :とぅっとるー、まゆしぃだよー

■ 再コンパイルする際の注意
フォルダ./sourceにソースコードが入っています。
再コンパイルを行う際は下記の順番でコンパイルしてください。

$ javac TalkText.java
$ javac BoardPoint.java
$ javac OthelloClient.java

■ 最後に
連日の障害続きで作る時間ほとんど取れなかった...orz
くそプログラムだけど遊んでくれると嬉しいです。
