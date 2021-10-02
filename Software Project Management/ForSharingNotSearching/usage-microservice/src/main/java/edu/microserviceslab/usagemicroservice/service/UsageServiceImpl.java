package edu.microserviceslab.usagemicroservice.service;

import edu.microserviceslab.usagemicroservice.entity.UsageStatistic;
import edu.microserviceslab.usagemicroservice.repo.UsageStatisticRepo;
import edu.microserviceslab.usagemicroservice.service.interfaces.UsageService;
import org.springframework.stereotype.Service;

import java.util.List;
import static java.lang.Math.toIntExact;

@Service
public class UsageServiceImpl implements UsageService {

    private UsageStatisticRepo usageStatisticRepo;

    public UsageServiceImpl(UsageStatisticRepo usageStatisticRepo) {
        this.usageStatisticRepo = usageStatisticRepo;
    }

    public UsageStatistic addUsageStatistic(UsageStatistic usageStatistic){
        List<UsageStatistic> ToParse = getUsageStatisticsPerVehicle(usageStatistic.getVehicleId());
        int indexToParse = toIntExact(usageStatistic.getVehicleId());

        usageStatistic.setDriverId(ToParse.get(indexToParse).getDriverId());
        usageStatistic.setDriverFullname(ToParse.get(indexToParse).getDriverFullname());
        usageStatistic.setVehicleLicensePlate(ToParse.get(indexToParse).getVehicleLicensePlate());
        return usageStatisticRepo.save(usageStatistic);
    }

    public List<UsageStatistic> getAllUsageStatistics() {
        return usageStatisticRepo.findAll();
    }

    public List<UsageStatistic> getUsageStatisticsPerDriver(Long driverId) {
        return usageStatisticRepo.findByDriverId(driverId);
    }

    public List<UsageStatistic> getUsageStatisticsPerVehicle(Long vehicleId) {
        System.out.println(usageStatisticRepo.findByVehicleId(vehicleId));
        return usageStatisticRepo.findByVehicleId(vehicleId);
    }
}
