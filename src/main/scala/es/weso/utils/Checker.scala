package es.weso.utils

import org.scalactic.{
 Or => SOr,
 _ 
}

/**
 * Checker of values of type +A with possible errors of type E
 * This is a facade 
 * 
 * Alternative implementations could be done using:
 * - scalaz (Validation)
 * - cats (Validated)
 *  
 */
case class Checker[+A,+E] private(value: A SOr Every[E]){
  
  /**
   * @return `true` if the value has no errors
   */
  def isOK: Boolean = value.isGood
  
  /**
   * Fold this checker into another value
   * @tparam V the result type 
   * @param ok function to apply to the value
   * @param err function to apply to the sequence of errors
   * @return the result of applying either the `ok` function or the `err` function 
   */
  def fold[V](ok: A => V, err: Seq[E] => V): V = 
    value.fold(ok,(es: Every[E]) => err(es.toSeq))
    
  /**
   * Build a new Checker by applying a function to the value
   * @tparam B the value type of the new Checker
   * @param f the function to apply to the current value
   * @return a new Checker with the new value or the list of existing errors
   */
  def map[B](f: A => B): Checker[B,E] = {
    Checker(value.map(f))
  }
  
  /**
   * Adds an Error to a Checker.
   * @param e error to add
   * @return If the value was ok, converts it into an error with the value `e`, 
   * 					otherwise, adds the error to the list of errors
   */
  def addError[E1 >: E](e:E1): Checker[A,E1] = {
    Checker(value.fold(
        _ => Bad(Every(e)),
        es => {
          val newEvery : Every[E1] = es :+ e 
          Bad(es :+ e)
        }
        ))
  }
  
  /**
   * Adds a list of errors to a Checker
   * @param es list of errors to add
   * @return a checker with the list of errors es
   */
  def addErrors[E1 >: E](es: Seq[E1]): Checker[A,E1] = {
    val zero : Checker[A,E1] = this
    es.foldRight(zero)((e,rest) => rest.addError(e))
  }
  
  /**
   * Returns the list of errors of this checker
   * <p> If the checker is ok, the list will be empty
   */
  def errors: Seq[E] = {
    this.fold(x => Seq(), es => es.seq)
  }
} 

object Checker {

  /**
   * Creates an ok checker from a value
   * @tparam A type of value
   * @param x value
   * @return Checker with an ok value 
   */
  def ok[A](x: A): Checker[A,Nothing] = 
    Checker(Good(x))
    
  /**
   * Creates a checker with an error
   * @tparam E type of error
   * @param e error value
   * @return the Checker that contains the error   
   */
  def err[E](e: E): Checker[Nothing,E] = 
    Checker(Bad(Every(e)))
    
  /**
   * Creates a checker with an error from an String
   * @tparam E type of error
   * @param msg String message
   * @return Checker that contains an error    
   */
  def errString[E >: Throwable](msg: String): Checker[Nothing,E] = 
    Checker(Bad(Every(throw new Exception(msg))))

  /**
   * accumulates a sequence of errors into a checker
   * @param es sequence of errors
   * @param checker checker
   * @return the new checker (if it was ok, it will contain the sequence of errors)
   */
  def accumErrors[A,E](es:Seq[E], checker: Checker[A,E]): Checker[A,E] = {
    checker.addErrors(es) 
  }

  /**
   * Add an error to a checker
   * @param e error to add
   * @param checker current checker
   * @return if the checker was ok, a checker with a single error, otherwise a checker 
   * with the new error accumulated
   */
  def accumErr[A,E](e:E, checker: Checker[A,E]): Checker[A,E] = {
    checker.addError(e)
  }
  
  /**
   * Checks a predicate on a value
   * @param x the value to check
   * @param p the predicate
   * @param name name of the condition to check
   * @param ferr a function that converts a String into an Error
   * @return if the value satisfies the predicate, a Checker with an ok value, otherwise the error that results of applying ferr to the name of the condition
   */
  def cond[A,E](x: A, p: A => Boolean, name: String)(implicit ferr: String => E): Checker[A,E] = {
    if (p(x)) ok(x)
    else err(ferr(s"Failed condition $name on $x"))
  }

/*  def cond[A,E >: Throwable](x: A,p: A => Boolean): Checker[A,E] = {
    if (p(x)) ok(x)
      else errString(s"Condition failed on $x")
  } */

  /**
   * Checks if a value satisfies at least one of the conditions
   * 
   * <p> This combinator returns a checker for the value but doesn't keep track
   * about which of the conditions was satisfied
   * 
   * @tparam A type of value
   * @tparam E type of Error
   * @param x value to check
   * @param conds sequence of conditions
   * @param e error in case none of the conditions is satisfied
   * @return a checker for value x that is ok if at least one of the conditions is satisfied
   */
  def checkSome[A,E](x: A, conds: Seq[A => Checker[A,E]], e: E): Checker[A,E] = {
    val zero : Checker[A,E] = err(e)
    conds.foldLeft(zero)((rest,cond) => if (cond(x).isOK) ok(x) else rest)
  }

  /**
   * Checks that all of the checkers are ok
   * @tparam A type of value
   * @tparam E type of Error
   * @param cs sequence of checkers
   * @return if all the checkers are ok, returns a checker with the list of values, otherwise, 
   * all the accumulated errors 
   */
  def checkAll[A,E](cs: Seq[Checker[A,E]]): Checker[Seq[A],E] = {
    def op : (Checker[A,E],Checker[Seq[A],E]) => Checker[Seq[A],E] = { (x,rest) => {
      x.fold(g => rest.fold(gs => ok(g +: gs), es => rest), es => accumErrors(es,rest))
    }}
    val zero: Checker[Seq[A],E] = ok(Seq())
    cs.foldRight(zero)(op)
  }
  
  /**
   * Checks that a value satisfies all of the conditions
   * 
   * @tparam A type of value
   * @tparam E type of Error
   * @param x value to check
   * @param conds a sequence of conditions
   * @return the value if all the conditions are ok, otherwise, the accumulated errors  
   */
  def checkValueAll[A,E](x: A, conds: Seq[A => Checker[A,E]]): Checker[A,E] = {
    def op : (A => Checker[A,E],Checker[A,E]) => Checker[A,E] = { (checker,rest) => {
      checker(x).fold(g => rest.fold(v => ok(v), es => rest), es => accumErrors(es,rest))
    }}
    val zero: Checker[A,E] = ok(x)
    conds.foldRight(zero)(op)
  }

  /**
   * Checks that a value satisfies all the conditions
   *  
   */
  def checkValueAllFold[A,B,E](x: A, conds: Seq[A => Checker[B,E]], combine: (B,B) => B, basic: A => B): Checker[B,E] = {
    def op : (A => Checker[B,E], Checker[B,E]) => Checker[B,E] = { (checker,rest) => {
      checker(x).fold(b1 => rest.fold(b2 => ok(combine(b1,b2)), es => rest), es => accumErrors(es,rest))
    }}
    val zero: Checker[B,E] = ok(basic(x))
    conds.foldRight(zero)(op)
  }

}
  
   
  
