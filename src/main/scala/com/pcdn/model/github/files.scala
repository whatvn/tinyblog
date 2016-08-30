package com.pcdn.model.github

/**
  * Created by Hung on 8/19/16.
  */
case class files(details: List[fileDetail])
case class fileDetail(sha: Option[String],
                      filename: String,
                      status: String,
                      additions: Int,
                      deletions: Int,
                      changes: Int,
                      blob_url: Option[String],
                      raw_url: Option[String],
                      contents_url: Option[String],
                      patch: Option[String])