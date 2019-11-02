## rediz - a toy redis client
---

![image](https://user-images.githubusercontent.com/601206/68068310-f8d3eb80-fd5b-11e9-8778-bdb2c0b71f93.png)

This is a redis client in Scala, implementing the [redis protocol](https://redis.io/topics/protocol) using FS2 sockets, scodec, ZIO (using zio-cats interop).

**This is a tiny toy example and nor ready for any production use!** Improvements always welcomed!

Inspired by the excellent work by [Rob Norris](https://twitter.com/tpolecat) on [Skunk](https://github.com/tpolecat/skunk) - a Postgres data access library.

### What's inside?

To have a better understanding at how this client works, I recommend watching Rob Norris' excellent talk [Pure Functional Database Programming‚ without JDBC](https://www.youtube.com/watch?v=NJrgj1vQeAI). It wonderfully explains how scodec is used together with FS2 sockets to parse raw TCP bytes in pure Scala.

This library builds on this effort, providing an implementation of the redis protocol, wrapped in a console app for sending and receiving redis commands.

### Running

To run this, clone the project and run the Main app.