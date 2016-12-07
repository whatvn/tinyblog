/**
  * Created by Hung on 10/25/16.
  */


trait A

case class B(b: String) extends A
case class C(c: String) extends A



object Test1 {



  def main(args: Array[String]): Unit = {
    val b = new B("bbb")
    val c = new C("ccc")

    val as = List(b, c)

    val cs = as.fold(new B(""))((x, y) => new C("") )

  }

}
