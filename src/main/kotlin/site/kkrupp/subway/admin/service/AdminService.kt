package site.kkrupp.subway.admin.service

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import site.kkrupp.subway.admin.SaveStationRequestDto
import site.kkrupp.subway.admin.domain.Admin
import site.kkrupp.subway.admin.dto.LoginReqDto
import site.kkrupp.subway.admin.repository.AdminRepository
import site.kkrupp.subway.bestroute.domain.BestRouteProblemAnswer
import site.kkrupp.subway.bestroute.repository.BestRouteRepository
import site.kkrupp.subway.files.service.FileService
import site.kkrupp.subway.fillblank.domain.FillBlankProblemAnswer
import site.kkrupp.subway.fillblank.repository.FillBlankRepository
import site.kkrupp.subway.station.domain.AliasName
import site.kkrupp.subway.station.domain.Station
import site.kkrupp.subway.station.domain.StationLine
import site.kkrupp.subway.station.repository.StationRepository
import java.util.*

@Service
class AdminService(
    val stationRepository: StationRepository,
    val fillBlankRepository: FillBlankRepository,
    val fileService: FileService,
    val bestRepository: BestRouteRepository,
    val adminRepository: AdminRepository,
    val bCryptPasswordEncoder: BCryptPasswordEncoder,
) {

    val logger = LoggerFactory.getLogger(this.javaClass)!!

    @Transactional
    fun getStationList(page: Int = 0): List<Station> {
        return stationRepository.findAll(PageRequest.of(page, 100, Sort.by("id"))).content
    }

    @Transactional
    fun saveStation(dto: SaveStationRequestDto): Station {
        val station =
            stationRepository.findById(dto.id.toString()).orElseThrow {
                IllegalArgumentException("Invalid station id")
            }
        station.name = dto.name
        val orgLines = station.lines.map { it.lineId }
        station.lines.removeIf { !dto.lines.contains(it.lineId) }
        dto.lines.forEach {
            if (!orgLines.contains(it)) {
                station.lines.add(StationLine(stationId = station.id, lineId = it))
            }
        }
        station.aliasName.clear()
        station.aliasName.addAll(dto.aliasNames.map { AliasName(name = it, stationId = station.id) })


        return stationRepository.save(station)

    }

    @Transactional
    fun searchFillBlankProblemsByStationName(searchName: String): List<FillBlankProblemAnswer> {
        val station = stationRepository.findByNameOrAliasName_Name(searchName, searchName)
            ?: return emptyList()
        val problems = station.map {
            val orgProblem = fillBlankRepository.findByAnswer_id(it.id)
            //TODO : 한 개역에 문제가 여러 개 있을 때 로직 처리.
            FillBlankProblemAnswer(
                id = orgProblem.firstOrNull()?.id ?: -1,
                problemImage = orgProblem.firstOrNull()?.problemImage ?: "",
                answer = it,
            )
        }
        return problems
    }

    @Transactional
    fun getNumberOfFillBlankProblems(): Int {
        return fillBlankRepository.count().toInt()
    }

    @Transactional
    fun getNumberOfStations(): Int {
        return stationRepository.count().toInt()
    }

    @Transactional
    fun getAllFillBlankProblems(page: Int = 0): List<FillBlankProblemAnswer> {
        val station = stationRepository.findAll(PageRequest.of(page, 100, Sort.by("id"))).content
        val problems = station.map {
            val orgProblem = fillBlankRepository.findByAnswer_id(it.id)
            //TODO : 한 개역에 문제가 여러 개 있을 때 로직 처리.
            FillBlankProblemAnswer(
                id = it.id.toInt(),
                problemImage = orgProblem.firstOrNull()?.problemImage ?: "",
                answer = it,
            )
        }
        return problems
    }

    @Transactional
    fun searchStationByName(searchName: String): List<Station> {
        return stationRepository.findByNameOrAliasName_Name(searchName, searchName) ?: emptyList()
    }

    fun editFillBlankProblem(id: Int? = null, answer: Long, problemImage: MultipartFile): FillBlankProblemAnswer {
        val fileUrl = fileService.uploadFile(problemImage, UUID.randomUUID().toString())
        return fillBlankRepository.save(
            FillBlankProblemAnswer(
                id = id ?: 0,
                problemImage = fileUrl,
                answer = stationRepository.findById(answer) ?: throw IllegalArgumentException("Invalid station id")
            )
        )
    }

    @Transactional
    fun saveBoardingCnt(stationId: Long, boardingCnt: Int) {
        val station = stationRepository.findById(stationId) ?: throw IllegalArgumentException("Invalid station id")
        station.boardingCnt = boardingCnt
        stationRepository.save(station)
    }

    @Transactional
    fun saveBoardingCntByName(stationName: String, boardingCnt: Int) {
        val findStation = stationRepository.findByNameOrAliasName_Name(stationName, stationName)
        if (findStation.isNullOrEmpty()) {
            throw IllegalArgumentException("Invalid station name")
        }
        findStation[0].boardingCnt = boardingCnt
        stationRepository.save(findStation[0])
    }

    @Transactional
    fun getBestRouteProblems(page: Int = 0): List<BestRouteProblemAnswer> {
        return bestRepository.findAll(PageRequest.of(page, 100, Sort.by("id"))).content
    }

    @Transactional
    fun getNumberOfBestRouteProblems(): Int {
        return bestRepository.count().toInt()
    }

    @Transactional
    fun join(loginReqDto: LoginReqDto) {
        adminRepository.save(
            Admin(
                username = loginReqDto.username,
                password = bCryptPasswordEncoder.encode(loginReqDto.password),
                role = "ADMIN"
            )
        )
    }

    @Transactional
    fun findStationAndEnrollFillblankProblem(stationName: String, problemImage: MultipartFile): FillBlankProblemAnswer {
        val station = stationRepository.findByNameOrAliasName_Name(stationName, stationName)
            ?: throw IllegalArgumentException("Invalid station name")
        if (station.size > 1) {
            throw IllegalArgumentException("Station name is ambiguous")
        }
        val problem = fillBlankRepository.findByAnswer_id(station[0].id)
        val fileUrl = fileService.uploadFile(problemImage, UUID.randomUUID().toString())


        logger.info("Station: ${station[0].name}, Problem Image: $fileUrl")

        //TODO : 한 개역에 문제가 여러 개 있을 때 로직 처리.
        if (problem.isNotEmpty()) {
            problem[0].problemImage = fileUrl
            fillBlankRepository.save(problem[0])
        } else {
            return fillBlankRepository.save(
                FillBlankProblemAnswer(
                    id = 0,
                    problemImage = fileUrl,
                    answer = station[0]
                )
            )
        }
        return problem[0]
    }
}