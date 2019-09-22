package rick.map

import org.scalatest.WordSpec

class TreeMapSpec extends WordSpec {

  "A Tree" when {
    "it has key" should {
      "find that key" in {
        val map = new TreeMap
        map.put("Rick", "Dog")
        assert(map.get("Rick") == "Dog")
      }
    }
    "it does not have a key" should {
      "not find that key" in {
        val map = new TreeMap
        assert(map.get("Rick") == null)
      }
    }

    "it has two keys added to it" should {
      "find both key" in {
        val map = new TreeMap
        map.put("Rick", "Dog")
        map.put("Diana", "Cat")
        assert(map.get("Rick") == "Dog")
        assert(map.get("Diana") == "Cat")
      }
    }

    "it has many keys added to it" should {
      "find all key" in {
        val map = new TreeMap

        val keys = Seq("Rick", "Sam", "Adam", "Zeek", "Billy", "Diana")
        keys.foreach(key => map.put(key, "Cat"))

        assert(keys.map(key => map.get(key)).count(_ == "Cat") == keys.size)

      }
    }
  }

  "it has many keys removed form it" should {
    " keep the key/values that were not removed" in {
      val map = new TreeMap

      val keys = Seq("Billy", "Zeek", "Adam", "Diana", "Rick", "Sam", "Louis") //, //"Kelly", "Bob")
      keys.foreach(key => map.put(key, "Cat"))

      assert(keys.map(key => map.get(key)).count(_ == "Cat") == keys.size)

      removeAndCheck(keys.head, keys.tail)

      def removeAndCheck(key: String, keys: Seq[String]): Unit = {
        map.remove(key)
        assert(keys.map(key => map.get(key)).count(_ == "Cat") == keys.size)
        if (keys.tail.nonEmpty) {
          removeAndCheck(keys.head, keys.tail)
        }
      }
    }
  }

  "it has many helper methods" should {
    " these should work " in {
      val map = new TreeMap

      val keys = Seq("Billy", "Zeek", "Adam", "Diana", "Rick", "Sam", "Louis", "Kelly", "Bob")
      keys.foreach(key => map.put(key, "Cat"))

      println(map.toString)

      map.walkMapInOrder((keyValue: KeyValue) => println(keyValue.getKey))
      map.walkMapPostOrder((keyValue: KeyValue) => println(keyValue.getKey))
      map.walkMapPreOrder((kv: KeyValue) => println(kv.getKey))

    }
  }




  "it removes a key and keeps the key/values that were not removed " should {

    def createMap(): (TreeMap, Seq[String]) = {
      val map = new TreeMap
      val keys = Seq("C", "A", "D", "B")
      keys.foreach(key => map.put(key, "Cat"))
      (map, keys)
    }

    " remove far left leaf key" in {

      /*
          +-----+
         /|     |
        | |  C  |\
        / +-----+ |
       /          \
      |----+    +--\---+
      |    |    |   |  |
      | A  |    |   D  |
      |    |    |      |
      +----+    +------+
       /
+----+ -/
| B  |/
|    |
+----+        Delete B
 */

      val (map,keys) = createMap()

      map.remove("B")

      assert(keys.map(key => map.get(key)).count(_ == "Cat") == keys.size-1)


    }


    " remove Left key with no right node " in {

      /*
          +-----+
         /|     |
        | |  C  |\
        / +-----+ |
       /          \
      |----+    +--\---+
      |    |    |   |  |
      | A  |    |   D  |
      |    |    |      |
      +----+    +------+
       /
+----+ -/
| B  |/
|    |
+----+        Delete A
 */

      val (map,keys) = createMap()

      map.remove("A")

      assert(keys.map(key => map.get(key)).count(_ == "Cat") == keys.size-1)




    }

    " remove root node  " in {

      /*
          +-----+
         /|     |
        | |  C  |\
        / +-----+ |
       /          \
      |----+    +--\---+
      |    |    |   |  |
      | A  |    |   D  |
      |    |    |      |
      +----+    +------+
       /
+----+ -/
| B  |/
|    |
+----+        Delete C
 */

      val (map,keys) = createMap()

      map.remove("C")

      assert(keys.map(key => map.get(key)).count(_ == "Cat") == keys.size-1)




    }

    " remove root node  DA has a left node" in {

      /*
                  +-----+
                 /|     |
                | |  C  |-\
                / +-----+  -\
               /             -\
              |----+           -\
              |    |             -\
              | A  |               -
              |    |            +------+
              +----+            |  DA  |
                  \-            |      |
                    \-          |      |
                    +-\--+      +-/---\+
                    |    |       |     \
                    |  B |       /      \                   -
                    +----+      |        \
                             +----+   +-----+
                             | D  |   |  DB |
                             |    |   |     |
                             +----+   +-----+
                             Delete C
 */

      val map = new TreeMap
      val keys = Seq("C", "A", "DA", "B", "DB", "D")

      keys.foreach(key => map.put(key, "Cat"))

      println(map)


      map.remove("C")

      println(map.toString)

      assert(keys.map(key => map.get(key)).count(_ == "Cat") == keys.size-1)






    }


    " remove root node  DA has only a right node" in {

      /*
                  +-----+
                 /|     |
                | |  C  |-\
                / +-----+  -\
               /             -\
              |----+           -\
              |    |             -\
              | A  |               -
              |    |            +------+
              +----+            |  DA  |
                  \-            |      |
                    \-          |      |
                    +-\--+      +-----\+
                    |    |             \
                    |  B |              \                   -
                    +----+               \
                                      +-----+
                                      |  DB |
                                      |     |
                                      +-----+
                             Delete C
 */

      val map = new TreeMap
      val keys = Seq("C", "A", "DA", "B", "DB")

      keys.foreach(key => map.put(key, "Cat"))

      println(map)


      map.remove("C")

      println(map.toString)

      assert(keys.map(key => map.get(key)).count(_ == "Cat") == keys.size-1)






    }


  }


}