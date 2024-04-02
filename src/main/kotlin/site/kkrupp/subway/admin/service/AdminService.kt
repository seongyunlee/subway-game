package site.kkrupp.subway.admin.service

import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import site.kkrupp.subway.admin.SaveStationRequestDto
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
) {

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
}