#tinyblog

A simple blog engine written in scala with spray, bootstrap and github as storage. The following technologies are used to build this blogging engine:

- Spray framework : Web server, routing, HTTP Client, Json deserializer, and caching use Spray library.
- Twirl : is a template engine to render HTML.
- MapDB: an embedded high performance databased written in Krotlin (a another JVM based language) to store commit history and blog metadata.
- Github: blog content was written in Markdown, then commit to Github under repository: http://github.com/whatvn/whatvn.github.io. There will be a worker (implemented inside blog engine to fetch data from Github and display content on this blog.
- Boostrap and Clean Blog template: I am not so good at HTML and design, so I pick this free/simple boostrap template to use. Thanks Clean blog author :).
- Pegdown: Markdown parser to parse markdown text format to html document (used to use Markwrap but it does not provide a method to build parser with timeout option).
- Akka: To build worker and logging stuff.

# Demo:

There’s a running demo version of this blogging engine here: http://40.76.35.190/ . I will move all my personal blog to use this blogging engine as soon as possible.

# License:

Do whatever you want
