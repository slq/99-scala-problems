val list = List(1, 1, 2, 3, 5, 8)

object P01 {
  def last[T](list: List[T]): T = list.last
}

class P01[T] {
  def last(list: List[T]): Option[T] = list match {
    case Nil => None
    case head :: Nil => Some(head)
    case head :: tail => last(tail)
  }
}

val p01 = new P01[Int]()
p01.last(list)
P01.last(list)

object P02 {
  def penultimate[T](list: List[T]): T = list match {
    case x1 :: x2 :: Nil => x1
    case List() => throw new NoSuchElementException()
    case x :: xs => penultimate(xs)
  }
}

P02.penultimate(list)


object P03 {
  def nth[T](n: Int, list: List[T]): Option[T] =
    if (n >= list.size) None
    else Some(list(n))
}

P03.nth(4, list)
P03.nth(1000, list)


object P04 {
  def length[T](list: List[T]): Int =
    list.length

  def length2[T](list: List[T]): Int = list match {
    case List() => 0
    case x :: xs => 1 + length2(xs)
  }

  def length3[T](list: List[T]): Int =
    list.foldLeft(0) { (acc, _) => acc + 1 }
}

P04.length(list)
P04.length2(list)
P04.length3(list)


object P05 {
  def reverse[T](list: List[T]): List[T] = list match {
    case List() => List()
    case x :: xs => reverse(xs) ++ List(x)
  }

  def reverse2[T](list: List[T]): List[T] =
    list.foldRight(List[T]()) { (elem, acc) => acc ++ List(elem) }

  def reverse3[T](list: List[T]): List[T] =
    list.foldLeft(List[T]()) { (acc, elem) => elem :: acc }

  def reverse4[T](list: List[T]): List[T] =
    list.reverse
}

P05.reverse(list)
P05.reverse2(list)
P05.reverse3(list)
P05.reverse4(list)


object P06 {
  def isPalindrome[T](list: List[T]): Boolean =
    list == list.reverse
}

P06.isPalindrome(List(1, 2, 3, 2, 1))
P06.isPalindrome(List(1, 2, 3, 2, 1, 1))


object P07 {
  def flatten(list: List[Any]): List[Any] =
    list.flatMap {
      case l: List[_] => flatten(l)
      case elem => List(elem)
    }

  def flatten2(list: List[Any]): List[Any] = list match {
    case (head: List[_]) :: tail => flatten2(head) ++ flatten2(tail)
    case head :: tail => head :: flatten2(tail)
    case Nil => Nil
  }
}

P07.flatten(List(List(1, 1), 2, List(3, List(5, 8))))
P07.flatten2(List(List(1, 1), 2, List(3, List(5, 8))))
P07.flatten2(List(List('a', 'b'), 2, List(3, List(5, 8))))

object P08 {
  def compress[T](list: List[T]): List[T] =
    list.foldRight(List[T]()) { (elem, acc) => if (acc.nonEmpty && elem == acc.head) acc else elem :: acc }

  def compress2[T](list: List[T]): List[T] = list match {
    case Nil => Nil
    case head :: tail => head :: compress2(tail.dropWhile(_ == head))
  }
}

P08.compress(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
P08.compress2(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))


object P09 {
  // warning partition vs span
  def pack[T](list: List[T]): List[List[T]] = list match {
    case Nil => Nil
    case head :: _ =>
      val (left, right) = list.partition(_ == head)
      left :: pack(right)
  }

  def pack2[T](list: List[T]): List[List[T]] = list match {
    case Nil => Nil
    case head :: _ =>
      val (left, right) = list.span(_ == head)
      left :: pack(right)
  }

  def pack3[T](list: List[T]): List[List[T]] = list match {
    case Nil => Nil
    case head :: _ =>
      val left = list.takeWhile(_ == head)
      val right = list.dropWhile(_ == head)
      left :: pack(right)
  }
}

P09.pack(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
P09.pack2(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
P09.pack3(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))


object P10 {
  def encode[T](list: List[T]): List[(Int, T)] =
    P09.pack2(list).map { p => (p.length, p.head) }
}

P10.encode(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))


object P11 {
  def encodeModified[T](list: List[T]): List[Any] =
    P09.pack2(list).map {
      p => if (p.length == 1) p.head else (p.length, p.head)
    }

  def encodeModified2[T](list: List[T]): List[Either[T, (Int, T)]] =
    P09.pack2(list).map {
      p => if (p.length == 1) Left(p.head) else Right((p.length, p.head))
    }
}

P11.encodeModified(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
P11.encodeModified2(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))


object P12 {
  def times[T](n: Int, elem: T): List[T] =
    (1 to n).foldLeft(List[T]()) { (acc, _) => elem :: acc }

  def decode[T](list: List[(Int, T)]): List[T] =
    list.foldRight(List[T]()) { (elem, acc) => times(elem._1, elem._2) ++ acc }

  def decode2[T](list: List[(Int, T)]): List[T] =
    list.flatMap { case (n, elem) => List.fill(n)(elem) }
}

P12.decode(List((4, 'a), (1, 'b), (2, 'c), (2, 'a), (1, 'd), (4, 'e)))
P12.decode2(List((4, 'a), (1, 'b), (2, 'c), (2, 'a), (1, 'd), (4, 'e)))


object P13 {
  def encodeDirect[T](list: List[T]): List[(Int, T)] = list match {
    case Nil => Nil
    case head :: _ =>
      val (left, right) = list.span(_ == head)
      (left.length, left.head) :: encodeDirect(right)
  }
}

P13.encodeDirect(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))


object P14 {
  def duplicate[T](list: List[T]): List[T] =
    list flatMap { elem => List(elem, elem) }
}

P14.duplicate(List('a, 'b, 'c, 'c, 'd))


object P15 {
  def duplicateN[T](n: Int, list: List[T]): List[T] =
    list flatMap {
      List.fill(n)(_)
    }
}

P15.duplicateN(3, List('a, 'b, 'c, 'c, 'd))


object P16 {
  def drop[T](n: Int, list: List[T]): List[T] =
    (1 to list.length).zip(list).foldRight(List[T]()) {
      case ((idx, elem), acc) => if (idx % n == 0) acc else elem :: acc
    }
}

P16.drop(3, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))


object P17 {
  def split[T](n: Int, list: List[T]): (List[T], List[T]) =
    (list.take(n), list.drop(n))

  def split2[T](n: Int, list: List[T]): (List[T], List[T]) =
    list.splitAt(n)
}

P17.split(3, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
P17.split2(3, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))


object P18 {
  def slice[A](i: Int, k: Int, list: List[A]): List[A] =
    list.slice(i, k)
}

P18.slice(3, 7, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))


object P19 {
  def rotate[T](n: Int, list: List[T]): List[T] = {
    def rotate(m: Int): List[T] =
      list.drop(m) ++ list.take(m)

    if (n > 0) rotate(n) else rotate(list.length + n)
  }
}

P19.rotate(3, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
P19.rotate(-2, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))


object P20 {
  def removeAt[T](n: Int, list: List[T]): (List[T], T) =
    list.zipWithIndex.foldRight((List[T](), list.head)) { case ((elem, idx), (acc, removed)) =>
      if (idx == n) (acc, elem) else (elem :: acc, removed)
    }

  def removeAt2[T](n: Int, list: List[T]): (List[T], Option[T]) = list.splitAt(n) match {
    case (pre, elem :: post) => (pre ::: post, Some(elem))
    case (pre, Nil) => (pre, None)
  }
}

P20.removeAt(1, List('a, 'b, 'c, 'd))
P20.removeAt(0, List('a, 'b, 'c, 'd))
P20.removeAt2(1, List('a, 'b, 'c, 'd))
P20.removeAt2(0, List('a, 'b, 'c, 'd))


object P21 {
  def insertAt[T](elem: T, n: Int, list: List[T]): List[T] = list.splitAt(n) match {
    case (pre, post) => pre ::: elem :: post
  }
}

P21.insertAt('new, 1, List('a, 'b, 'c, 'd))
P21.insertAt('new, 3, List('a, 'b, 'c, 'd))


object P22 {
  def range(min: Int, max: Int): List[Int] =
    min to max toList

  def range2(min: Int, max: Int): List[Int] =
    List.range(min, max + 1)
}

P22.range(4, 9)
P22.range2(4, 9)
