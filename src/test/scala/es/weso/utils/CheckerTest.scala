package es.weso.utils

import org.scalatest._
import org.scalatest.prop.PropertyChecks
import org.scalatest.prop.Checkers
import SeqUtils._
import Checker._

class CheckerTest extends FunSpec with Matchers with Checkers {

  describe("Checker") {
    
    it("Checks simple ok") {
      val x2: Checker[Int,String] = ok(2)
      x2.isOK should be(true)
    }
    
    it("Checks simple error") {
      val e: Checker[Int,String] = err("Error")
      e.isOK should be(false)
    }
    
    it("Converts an ok value into an error") {
      val v: Checker[Int,String] = ok(2)
      val e = v.addError("Hi")
      e.isOK should be(false) 
      e.errors should contain only("Hi")
    }
    
    it("Accumulates several errors") {
      val v: Checker[Int,String] = ok(2)
      val e = v.addErrors(Seq("Hi","man"))
      e.isOK should be(false) 
      e.errors should contain only("Hi", "man")
    }

    it("should be able to fold an ok checker") {
      val v: Checker[Int,String] = ok(2)
      val folded = v.fold(x => x + 1, _ => 0)
      folded should be(3)
    }
    
    it("should be able to fold an errored checker") {
      val v: Checker[Int,String] = err("hi")
      val folded = v.fold(x => x + 1, _ => 0)
      folded should be(0)
    }
    
    it("should be able to check some conditions when all pass") {
      val isEven : Int => Checker[Int,String] = (x) => if (x % 2 == 0) ok(x) else err("not even")
      val isPositive : Int => Checker[Int,String] = (x) => if (x > 0) ok(x) else err("not positive")
      val checker = checkSome(2,Seq(isPositive,isEven),"none")
      checker.isOK should be(true)
    }
    
    it("should be able to check some conditions when some fail") {
      val isEven : Int => Checker[Int,String] = (x) => if (x % 2 == 0) ok(x) else err("not even")
      val isPositive : Int => Checker[Int,String] = (x) => if (x > 0) ok(x) else err("not positive")
      val checker = checkSome(3,Seq(isPositive,isEven),"none")
      checker.isOK should be(true)
    }

    it("should be able to check some conditions when all fail") {
      val isEven : Int => Checker[Int,String] = (x) => if (x % 2 == 0) ok(x) else err("not even")
      val isPositive : Int => Checker[Int,String] = (x) => if (x > 0) ok(x) else err("not positive")
      val checker = checkSome(-3,Seq(isPositive,isEven),"none")
      checker.isOK should be(false)
      checker.errors should contain only("none")
    }
    
    it("should be able to check all conditions when all pass") {
      val isEven : Int => Checker[Int,String] = (x) => if (x % 2 == 0) ok(x) else err("not even")
      val isPositive : Int => Checker[Int,String] = (x) => if (x > 0) ok(x) else err("not positive")
      val checker = checkValueAll(2,Seq(isPositive,isEven))
      checker.isOK should be(true)
    }
    
    it("should be able to check all conditions when some fail") {
      val isEven : Int => Checker[Int,String] = (x) => if (x % 2 == 0) ok(x) else err("not even")
      val isPositive : Int => Checker[Int,String] = (x) => if (x > 0) ok(x) else err("not positive")
      val checker = checkValueAll(3,Seq(isPositive,isEven))
      checker.isOK should be(false)
      checker.errors should contain only("not even")
    }
    
    it("should be able to check all conditions when all fail") {
      val isEven : Int => Checker[Int,String] = (x) => if (x % 2 == 0) ok(x) else err("not even")
      val isPositive : Int => Checker[Int,String] = (x) => if (x > 0) ok(x) else err("not positive")
      val checker = checkValueAll(-3,Seq(isPositive,isEven))
      checker.isOK should be(false)
      checker.errors should contain only("not even", "not positive")
    }

  }

}