import com.pcdn.model.database.{BlogMetadata, CommitHistory, Tags}
import com.pcdn.model.utils.{Hash, Settings}
import org.scalatest.FlatSpec

/**
  * Created by Hung on 1/9/17.
  */
class BlogSpec extends FlatSpec with Hash with Settings {

  val title = "Some title"
  val updateTime = "2016-08-31T04:21:25Z"
  val fileName = "/_post/some_example_article"
  val author = "Hung"
  val desc = "What's great about scala"


  "Commit history " should "be set and get properly" in {
    val testSHA = "f26efe8a9b1ce42b2528dc8c9b32759658d2cd9a"

    CommitHistory.update(fileName, testSHA)
    val output= CommitHistory.get(fileName).get
    assert(CommitHistory.isProcessed(fileName, testSHA))
    assert(CommitHistory.get(fileName).get contains toLong(testSHA))
    assert(output(0) == toLong(testSHA))
  }

  "Tags " should "be set and get properly" in {
    val demoTag = "scalaTest"
    Tags.update(demoTag, fileName)
    // test duplicated
    Tags.update(demoTag, fileName)
    val output = Tags.get(demoTag).get
    assert(output.length == 1)
    assert(output(0) == fileName)
  }

  "Blog metadata " should "be set and get successfully" in {
    val metadata = BlogMetadata(title, author, updateTime, desc, fileName)
    BlogMetadata.putMetadata(fileName, metadata)
    val output = BlogMetadata.getMetadata(fileName).get
    assert(BlogMetadata.getAll contains toHexString(fileName))
    assert(output == metadata)
    assert(output.author == author)
    assert(output.desc == desc)
    assert(output.updateTime == updateTime)
    assert(output.title == title)
    assert(output.url == fileName)
  }

  "Delete db " should "not throw exception" in {
    BlogMetadata.clear
    CommitHistory.clear
    Tags.clear
  }

  "Database " should "be empty by now" in {
    assert(BlogMetadata.getAll == Nil)
    assert(CommitHistory.getAll == Nil)
    assert(Tags.getAll == Nil)
  }

}
