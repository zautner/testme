package name.zautner.vitaly.appthis.svc.impl

import javax.inject.Inject

import name.zautner.vitaly.appthis.dao.model.{EndPoint, User}
import name.zautner.vitaly.appthis.dao.proto.{EndPointRepository, UserRepository}
import name.zautner.vitaly.appthis.svc.Model.EndPointDTO
import name.zautner.vitaly.appthis.svc.proto.{CoDecStrategy, EndPointService}
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service("endPointService")
class EndPointServiceImpl @Inject()(coDecStrategy: CoDecStrategy,
                                    endPointDao: EndPointRepository,
                                    userDao: UserRepository) extends EndPointService {
    private val logger = LoggerFactory.getLogger(getClass)

    @Transactional(readOnly = true)
    override def listEndpointsForUser(userId: Long, urlPrefix: String): Iterable[EndPointDTO] = endPointDao.listForUser(userId).map((endPoint: EndPoint) => {
        logger.info(s"listForUser($userId, $urlPrefix) started")
        val adjustedUrl = s"$urlPrefix/${coDecStrategy.code(endPoint.getId)}"
        EndPointDTO(endPoint.getUrl, adjustedUrl)
    })

    @throws[IllegalArgumentException]("No enity is found")
    @Transactional(readOnly = false)
    override def modifyEndPointForUser(alias: String, userId: Long, newUrl: String): String = {
        logger.info(s"modifyEndPointForUser($alias, $userId) started")
        endPointDao
          .findById(coDecStrategy.decode(alias))
          .getOrElse(throw new IllegalArgumentException("No End Point is found"))
          .setUrl(newUrl)
        alias
    }

    @throws[IllegalArgumentException]("No enity is found")
    @Transactional(readOnly = false)
    override def deleteEndPointForUser(alias: String, userId: Long): Unit = {
        logger.info(s"deleteEndPointForUser($alias, $userId) started")
        val endPoint: EndPoint = endPointDao
          .findById(coDecStrategy.decode(alias))
          .getOrElse(throw new IllegalArgumentException("No End Point is found"))
        endPoint.getUsers.remove(userDao.findById(userId).getOrElse(throw new IllegalArgumentException("No User is found")))
        endPointDao.save(endPoint)
        logger.info(s"deleteEndPointForUser($alias, $userId) finished")
    }

    @throws[IllegalArgumentException]("No user is found")
    @Transactional(readOnly = false)
    override def createEndPointForUser(url: String, userId: Long): String = {
        logger.info(s"deleteEndPointForUser($url, $userId) started")
        val user: User = userDao.findById(userId).getOrElse(throw new IllegalArgumentException("No user is found"))
        val endPoint = endPointDao.findEntryPointByUrl(url).getOrElse({
            val ep = new EndPoint
            ep.setUrl(url)
            ep
        })
        if (endPoint.getUsers.asScala.exists(userId == _.getId)) {
            logger.info(s"deleteEndPointForUser($url, $userId) interrupted")
            throw new IllegalArgumentException("Duplicate end-point definition!")
        }
        endPoint.getUsers.add(user)
        endPointDao.save(endPoint)
        logger.info(s"deleteEndPointForUser($url, $userId) finished")
        coDecStrategy.code(endPoint.getId)
    }

    @throws[IllegalArgumentException]("No end-point is found")
    @Transactional(readOnly = true)
    override def translateAlias(alias: String): String = {

        logger.info(s"deleteEndPointForUser($alias) started")
        val id = coDecStrategy.decode(alias)
        endPointDao.findById(id) match {
            case Some(bookmark: EndPoint) => {
                logger.info(s"deleteEndPointForUser($alias) finished")
                bookmark.getUrl
            }
            case None => {
                logger.error(s"deleteEndPointForUser($alias) interrupted")
                throw new IllegalArgumentException("URL does not exist for this user")
            }
        }
    }

}