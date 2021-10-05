package org.flinklab.util

import org.apache.flink.api.java.utils.ParameterTool

class Parameters(val args: Array[String]) {
  val params = ParameterTool.fromArgs(args)

  def get(k: String): Option[String] = {
    if (params.has(k)) {
      Some(params.get(k))
    } else {
      None
    }
  }

  def getOrElse(k: String)(default: String): String = {
    get(k).getOrElse(default)
  }

  def expect(k: String): String = {
    get(k).getOrElse(
      throw new RuntimeException(s"Expected parameter `$k` missing")
    )
  }

  def getOrEnv(k: String)(env: String): Option[String] = {
    get(k).orElse(sys.env.get(env))
  }

  def getOrEnvOrElse(k: String)(env: String)(default: String): String = {
    get(k).orElse(sys.env.get(env)).getOrElse(default)
  }

  def getOrExpectEnv(k: String)(env: String): String = {
    getOrEnv(k)(env).getOrElse(
      throw new RuntimeException(
        s"Expected parameter `$k` or environment variable `$env` missing"
      )
    )
  }
}

