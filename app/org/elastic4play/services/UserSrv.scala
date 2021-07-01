package org.elastic4play.services

import scala.concurrent.Future

import play.api.libs.json.JsObject
import play.api.mvc.RequestHeader

import org.elastic4play.{ AuthenticationError, AuthorizationError }

abstract class Role(val name: String)

trait AuthContext {
  def userId: String
  def userName: String
  def requestId: String
  def roles: Seq[Role]
}

trait UserSrv {
  def getFromId(request: RequestHeader, userId: String): Future[AuthContext]
  def getFromUser(request: RequestHeader, user: User): Future[AuthContext]
  def getInitialUser(request: RequestHeader): Future[AuthContext]
  def inInitAuthContext[A](block: AuthContext ⇒ Future[A]): Future[A]
  def get(userId: String): Future[User]
}

trait User {
  val attributes: JsObject
  val id: String
  def getUserName: String
  def getRoles: Seq[Role]
}

object AuthCapability extends Enumeration {
  type Type = Value
  val changePassword, setPassword, authByKey = Value
}

trait AuthSrv {
  val name: String
  val capabilities = Set.empty[AuthCapability.Type]

  def authenticate(username: String, password: String)(implicit request: RequestHeader): Future[AuthContext] = Future.failed(AuthenticationError("Operation not supported"))
  def authenticate(key: String)(implicit request: RequestHeader): Future[AuthContext] = Future.failed(AuthenticationError("Operation not supported"))
  def authenticate()(implicit request: RequestHeader): Future[AuthContext] = Future.failed(AuthenticationError("Operation not supported"))
  def changePassword(username: String, oldPassword: String, newPassword: String)(implicit authContext: AuthContext): Future[Unit] = Future.failed(AuthorizationError("Operation not supported"))
  def setPassword(username: String, newPassword: String)(implicit authContext: AuthContext): Future[Unit] = Future.failed(AuthorizationError("Operation not supported"))
  def renewKey(username: String)(implicit authContext: AuthContext): Future[String] = Future.failed(AuthorizationError("Operation not supported"))
  def getKey(username: String)(implicit authContext: AuthContext): Future[String] = Future.failed(AuthorizationError("Operation not supported"))
  def removeKey(username: String)(implicit authContext: AuthContext): Future[Unit] = Future.failed(AuthorizationError("Operation not supported"))
}