import com.github.silencesu.Easy3dNav.Easy3dNav;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author stan
 * @date 2022/10/18
 */
public class NavTest {


    private static final String dir = "C:\\Soft\\recastnavigation\\RecastDemo\\Bin\\";

    /**
     * 测试正常寻路
     */
    @Test
    public void testFind() throws IOException {
        Easy3dNav nav = initFile("dungeon.bin");
        // 使用寻路接口，寻路
        float[] start = {39.6f, 10.0f, 0.6f};
        float[] end = {17.3f, 10.0f, 1.6f};
        List<float[]> paths = nav.find(start, end);

        System.out.println("total paths: " + paths.size());
        for (float[] path : paths) {
            System.out.format("(%.1f, %.1f, %.1f)%n", path[0], path[1], path[2]);
        }
    }


    /**
     * 测试
     *
     * @throws Exception
     */
    @Test
    public void testFindNarrowPath() throws Exception {
        Easy3dNav nav = initFile("vrsport-cut02.bin");
        // 使用寻路接口，寻路
        float[] start = {1148.f, 0.0f, 50f};
        float[] end = {1153.f, 0.0f, 49.7f};
        List<float[]> paths = nav.find(start, end);

        for (float[] path : paths) {
            System.out.println(formatV3(path));
        }

    }


    /**
     * 测试查找多边形
     *
     * @throws Exception
     */
    @Test
    public void testFindPoly() throws Exception {
        Easy3dNav nav = initFile("vrsport-cut04.bin");

        float[] extend = {0.0f, 0.0f, 0.0f};
        List<float[]> points = new ArrayList<>();
        points.add(new float[]{1148.9495f, 0.4239f, 50.4684f});
        points.add(new float[]{1148.9495f, 0.4239f, 51.4684f});
        points.add(new float[]{1148.9495f, 0.4239f, 52.4684f});
        points.add(new float[]{1148.9495f, 0.4239f, 53.4684f});
        points.add(new float[]{1148.9495f, 0.4239f, 54.4684f});
        points.add(new float[]{1148.9495f, 0.4239f, 55.4684f});
        for (float[] point : points) {
            List<Long> polys = nav.getQuery().queryPolygons(point, extend, nav.getFilter());
            String originPoint = formatV3(point);
            if (polys == null || polys.size() == 0) {
                System.out.format("%s not found %n", originPoint);
            } else {
                System.out.format("%s found %n", originPoint);
            }
        }

    }


    /**
     * 查找最近的距离
     */
    @Test
    public void testFindClosedPoly() throws IOException {
        Easy3dNav nav = initFile("vrsport-23-v2.bin");
        // 使用寻路接口，寻路

        float[] extend = {0.0f, 0.0f, 0.0f};
        // float[] extend = {0.3f, 0.3f, 0.3f};

        List<float[]> points = new ArrayList<>();
        points.add(new float[]{1129.9189f, 0.5228f, 8.5214f});
        points.add(new float[]{1128.9189f, 0.5228f, 8.5214f});

        for (float[] point : points) {
            float[] nearest = nav.findNearest(point, extend);

            String originPoint = formatV3(point);

            if (nearest == null) {
                System.out.format("%s not found %n", originPoint);
            } else {
                System.out.format("%s found %s %n", originPoint, formatV3(nearest));
            }
        }
    }/*
    (1148.90, 0.40, 54.50) found (1147.00, 0.50, 53.50)
    (1148.95, 0.42, 54.47) found (1146.98, 0.52, 53.50)
    */

    private static String formatV3(float[] point) {
        return String.format("(%.4f, %.4f, %.4f)", point[0], point[1], point[2]);
    }/*

     */


    @Test
    public void testRandomPoint() throws Exception {
        //初始化寻路对象
        Easy3dNav nav = initFile("solo_navmesh.bin");
        for (int i = 0; i < 10; i++) {
            float[] random = nav.findRandom();
            System.out.printf("第%s次查找 %s, 可走坐标 %s %n",
                    i, formatV3(random), formatV3(nav.findNearest(random)));
        }
    }


    /**
     * 测试虚拟空间的寻路结果
     */
    @Test
    public void testSport() throws Exception {
        //初始化寻路对象
        Easy3dNav nav = initFile("vrsport-cut04.bin");

        // 使用寻路接口，寻路
        float[] start = {1148.7f, 0.400f, 51.5f};
        float[] end = {1148.7f, 0.400f, 58.4f};

        List<float[]> paths = nav.find(start, end);

        System.out.println("total paths: " + paths.size());
        for (float[] path : paths) {
            System.out.format("(%.1f, %.1f, %.1f)%n", path[0], path[1], path[2]);
        }
    }


    private static Easy3dNav initFile(String file) throws IOException {
        //初始化寻路对象
        Easy3dNav nav = new Easy3dNav();
        nav.setUseU3dData(false); // 默认为true，可以忽略
        nav.setPrintMeshInfo(false); // 默认为false，查看需要设置为true
        nav.init(dir + file);
        return nav;
    }

    private static Easy3dNav initUrl(String url) throws IOException {
        //初始化寻路对象
        Easy3dNav nav = new Easy3dNav();
        nav.setUseU3dData(false); // 默认为true，可以忽略
        nav.setPrintMeshInfo(false); // 默认为false，查看需要设置为true
        nav.initByUrl(url);
        return nav;
    }
}
