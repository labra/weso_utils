package es.weso.utils

/**
 * Very common utils on booleans
 * 
 * These functions are probably implemented elsewhere
 */
object Boolean {
  
def all(vs: Traversable[Boolean]): Boolean = {
    vs.fold(true)(_ && _)
}
  
def some(vs: Traversable[Boolean]): Boolean = {
    vs.fold(false)(_ || _)
}

}
