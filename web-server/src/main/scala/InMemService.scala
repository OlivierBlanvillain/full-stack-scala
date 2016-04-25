package server

import model._

object InMemService extends Api {
  private var memStorage: Seq[Todo] = Nil
  
  def store(data: Seq[Todo]): Unit = {
    println(s"Storing $data")
    memStorage = data
  }
  
  def load(): Seq[Todo] = {
    println(s"Loading $memStorage")
    memStorage
  }
}
