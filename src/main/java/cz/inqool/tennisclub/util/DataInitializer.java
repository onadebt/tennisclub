package cz.inqool.tennisclub.util;


import cz.inqool.tennisclub.config.AppProperties;
import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.model.SurfaceType;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.CourtDao;
import cz.inqool.tennisclub.infrastructure.dao.interfaces.SurfaceTypeDao;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer {

    private final AppProperties appProperties;
    private final SurfaceTypeDao surfaceTypeDao;
    private final CourtDao courtDao;

    public DataInitializer(AppProperties appProperties, SurfaceTypeDao surfaceTypeDao, CourtDao courtDao) {
        this.appProperties = appProperties;
        this.surfaceTypeDao = surfaceTypeDao;
        this.courtDao = courtDao;
    }

    @PostConstruct
    @Transactional
    public void init() {
        if (!appProperties.isDataInit()) {
            return;
        }

        System.out.println(
                """
                
                
                Initializing sample data which will never give you up and never let you down as well...
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⡿⣿⣿⡿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⣻⣻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣟⡽⣯⣻⣻⡽⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⣻⣻
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⡿⣿⣿⣿⣿⣿⣿⡿⣻⣻⣻⣻⣻⣻⡽⣯⣟⢷⠍⠟⠉⠛⢿⢿⣻⣻⢿⣿⣿⣯⣻⡽⣯⣻⣻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣻⢯
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣻⣻⣻⣻⡟⡅⠀⠀⠀⠠⠀⠀⠆⡹⣻⣻⡽⣯⣻⡽⣯⣻⡽⣻⣻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣻⣻⣻
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣻⣿⡟⡛⡜⡜⣎⢦⢶⣖⡴⡀⠠⣿⣿⣿⣟⣟⣟⣟⣟⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣻⣻⣻⣻
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣻⣻⢆⢭⢎⢎⢞⡝⣝⡽⡽⡣⢂⣟⢯⢯⢯⣿⣻⣻⡽⣻⡽⣻⣻⣿⣿⣿⣿⣿⣿⣿⡿⣟⣿⣿⣿⣿⣻
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⣟⢧⡒⡔⢆⢯⢎⠚⡜⡇⣼⣿⣿⣯⣻⣻⣻⣻⢯⣿⣿⣻⣻⣻⣻⢿⣿⣿⣿⣿⡿⣻⣻⣻⣟⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⢹⢧⢣⢣⠡⡋⡯⣫⢯⡹⣹⣿⣿⣿⣿⣯⣻⣻⣻⣿⣿⣻⣻⣻⣿⣟⣟⢿⣿⣿⣿⣿⣻⢿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠧⢣⢢⢌⣍⡹⡽⣹⣽⣿⣿⣿⣿⣿⡽⣯⣻⢯⣻⢯⣻⣻⣿⣿⣿⣿⣻⣻⣻⣻⢿⢿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣟⡽⣍⢎⢎⢝⢏⢏⣝⢿⣿⣿⣿⣿⣿⣿⣻⡽⣯⣻⣻⣿⣿⣟⢿⣿⢿⣻⣻⣿⣿⢿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⣿⣿⣟⣟⣟⡜⡜⡜⡝⡭⣫⢫⠂⢫⣿⣿⣿⣟⢯⣻⣻⣻⡽⣻⣿⣿⣿⣟⣿⣿⣿⣻⣟⣟⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⢿⡿⣿⢿⡿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⣿⣿⣿⡿⡽⡻⡿⣇⢣⢣⠱⡱⡱⣽⣿⠀⠀⠀⠀⠐⢉⠍⡛⢿⢯⣻⣻⣿⣿⡿⣿⣿⣿⣿⣟⣟⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣟⢿⣿⣿⣿⡿⣿⣿⣟⢿⣻⣻⡿⣏⢋⠀⠀⠀⣹⣻⡇⢣⠱⣥⣻⣿⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹⣿⣿⣻⣿⣿⣿⣟⣟⣟⡽⣻⣿⡿⡿⣿⣿⣿
                ⣿⣿⣿⣿⣿⢿⣿⣿⣿⢿⣻⣿⢿⣿⣿⢿⣻⣻⣻⡃⠀⠀⠀⠀⠀⠀⠠⠠⡣⢢⠱⡉⠙⠛⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣻⡽⣻⣿⢯⣻⣿⣿⢯⣻⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⢿⣻⣻⣿⣟⣟⣟⣿⣿⣿⣿⣿⡿⣟⣟⠄⠀⠀⠀⠀⠀⠀⠀⢀⢆⡑⠡⠉⠋⠖⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣻⢯⣻⡽⣻⣻⡿⣯⢿⣿⣿⣿⣿⣿
                ⣿⣻⣟⣟⣿⣿⣿⣿⣟⣟⣟⣟⣿⣿⣿⣿⣟⣟⡽⡄⠀⠀⠀⠀⠀⠀⠀⢀⠁⣯⠚⠹⠶⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⣿⣻⢯⢯⣻⣿⣿⣻⣻⣻⣿⣿⣿⣿⣿
                ⣿⣟⢿⣿⣿⣿⣿⣿⣻⣿⡿⣻⣻⣿⣿⣿⢿⣻⢯⠀⠀⠀⠀⠀⠀⠀⠀⠀⠛⣟⠖⡖⡤⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢻⢿⣻⣿⣻⣿⣿⣿⣿⣿⣻⢯⣻⣻⣻
                ⣿⣻⣻⣿⣿⣿⣿⣻⣽⣿⣿⣟⣟⢿⣿⣿⡿⣻⣻⠀⠀⠀⠀⠀⠀⠀⠀⠀⢦⢢⣠⣀⠀⠀⠀⠀⠩⡛⡝⡜⡖⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⢿⣿⣻⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣻⣻⣻⣿⣿⡿⣻⣿⣿⣻⣻⣿⣿⡿⣿⣻⣻⣻⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⡜⠈⠁⠀⠀⠀⠀⠀⠌⣌⢎⡜⡜⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣻⣿⣿⡿⣟⢿⣿⣿⣿
                ⣟⣿⣿⣿⡽⡽⡽⣻⣹⡽⣿⣿⣿⣻⣻⣻⣻⡽⣻⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⢢⠣⠒⠀⠀⠀⠀⠀⠀⠎⢎⢎⢎⢎⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣟⡽⣿⣿⣻⣻⣻⢿⣿⣿
                ⣿⣿⢿⣿⣯⣫⣏⢯⣫⣿⣿⣿⣿⣟⣟⣟⣟⡽⡽⠀⡀⠀⠀⠀⠀⢀⢀⠀⠰⡰⠤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡝⡽⡽⣿⣿⣿⣻⡝⡽
                ⣯⣯⣯⣯⢯⣫⢫⣻⡿⣻⣿⣿⣿⣿⣿⣻⡽⡽⣭⠂⠀⡰⡱⠡⠢⢂⠆⠀⢠⠰⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⢯⢫⣫⡿⣻⣿⣿⣿⣻⡹
                ⡿⡿⣻⣻⣻⢭⣚⢧⢫⣻⣿⣿⡿⡽⡽⡽⡽⣹⣝⢇⠄⠀⠀⠄⠄⠄⡐⠀⠄⡐⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡝⣝⡽⣹⢽⢯⡻⣻⣟⢯⢫⣚⣟⣟⣟⣟⣟⣟⡝
                ⣯⣻⡽⣯⣻⡜⡵⡽⣎⢭⣻⡝⡽⣽⡽⣝⣝⣝⡝⣗⢭⢎⠀⠀⠂⠂⠀⠀⠀⡐⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣹⣝⣝⡝⣝⡽⡽⡹⣚⠵⡭⢯⢯⢯⣻⡽⡽⣣
                ⣟⣟⡽⣯⢯⢎⢎⢯⣏⡗⡝⣝⡽⣻⢯⣫⢫⢫⣫⣻⢯⡳⡱⡱⡱⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡝⡝⡝⣝⡝⡝⡭⣫⢫⢭⣚⣝⣝⣝⡽⣹⣹⢧
                ⢏⠯⢫⢫⢫⢪⢎⢯⢏⠳⡹⡹⣻⡿⡯⣫⢫⡹⡹⡽⡽⡹⡸⡜⡄⠀⠀⢀⢂⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡭⡭⣫⡹⡹⡭⣫⢫⢫⣚⡜⡝⡝⣝⣝⢽⡹⡭
                """
        );



        SurfaceType clay = new SurfaceType("clay", new BigDecimal("1.00"));
        SurfaceType grass = new SurfaceType("grass", new BigDecimal("1.50"));
        surfaceTypeDao.save(clay);
        surfaceTypeDao.save(grass);

        courtDao.save(new Court("1", clay));
        courtDao.save(new Court("2", clay));
        courtDao.save(new Court("3", grass));
        courtDao.save(new Court("4", grass));
    }
}

