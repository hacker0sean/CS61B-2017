import org.junit.Test;

import java.util.*;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.
    public double[] distance_per_pixel_longitude_per_depth = new double[8];
    public final static int MaxDepth = 7;
    public final static int RootDepth = 0;
    public int DepthExpect = -1;
    public double lrlon_of_query;
    public double ullon_of_query;
    public double w;
    public double h;
    public double ullat_of_query;
    public double lrlat_of_query;
    public QuadTree root;
    public String imgroot;

    public class QuadTree implements Iterable<QuadTree>, Comparable<QuadTree> {
        private String filename;
        private int depth;
        private double ullat;
        private double ullon;
        private double lrlat;
        private double lrlon;
        private double midlat;
        private double midlon;
        private double distance_per_pixel_longitude;
        private double distance_per_pixel_feet;
        public QuadTree up_left;
        public QuadTree up_right;
        public QuadTree low_left;
        public QuadTree low_right;

        public QuadTree(double ullat, double ullon, double lrlat, double lrlon, String filename){
            this.ullat = ullat;
            this.ullon = ullon;
            this.lrlat = lrlat;
            this.lrlon = lrlon;
            this.filename = filename;
            midlat = (ullat + lrlat) / 2.0;
            midlon = (ullon + lrlon) / 2.0;
            if (filename.equals("root"))
                depth = RootDepth;
            else {
                depth = filename.length();
            }
            distance_per_pixel_longitude = (lrlon - ullon) / MapServer.TILE_SIZE;
            distance_per_pixel_feet = distance_per_pixel_longitude * 288200;
            if (distance_per_pixel_longitude_per_depth[depth] == 0){
                distance_per_pixel_longitude_per_depth[depth] = distance_per_pixel_longitude;
            }
        }

        public String getFilename(){
            return filename;
        }

        public int getDepth(){
            return depth;
        }

        public double getUllat(){
            return ullat;
        }

        public double getUllon(){
            return ullon;
        }

        public double getLrlat(){
            return lrlat;
        }

        public double getLrlon(){
            return lrlon;
        }

        public double getMidlat(){
            return midlat;
        }

        public double getMidlon(){
            return midlon;
        }

        public double getDistance_per_pixel_longitude(){
            return distance_per_pixel_longitude;
        }

        public double getDistance_per_pixel_feet(){
            return distance_per_pixel_feet;
        }


        @Override
        public Iterator<QuadTree> iterator() {
            List<QuadTree> x = new ArrayList<>();
            x.add(up_left);
            x.add(up_right);
            x.add(low_left);
            x.add(low_right);
            return x.iterator();
        }

        @Override
        public int compareTo(QuadTree quadTree) {
            double x = this.ullat - quadTree.ullat;
            if (x > 0)
                return -1;
            else if (x == 0)
                return 0;
            else
                return 1;
        }
    }

    public void InitUp_Left(QuadTree T){
        String filename;
        if (T.getFilename().equals("root"))
            filename = "1";
        else
            filename = T.getFilename() + "1";
        T.up_left = new QuadTree(T.getUllat(), T.getUllon(), T.getMidlat(), T.getMidlon(), filename);
    }

    public void InitUp_Right(QuadTree T){
        String filename;
        if (T.getFilename().equals("root"))
            filename = "2";
        else
            filename = T.getFilename() + "2";
        T.up_right = new QuadTree(T.getUllat(), T.getMidlon(), T.getMidlat(), T.getLrlon(), filename);
    }

    public void InitLow_Left(QuadTree T){
        String filename;
        if (T.getFilename().equals("root"))
            filename = "3";
        else
            filename = T.getFilename() + "3";
        T.low_left = new QuadTree(T.getMidlat(), T.getUllon(), T.getLrlat(), T.getMidlon(), filename);
    }

    public void InitLow_right(QuadTree T){
        String filename;
        if (T.getFilename().equals("root"))
            filename = "4";
        else
            filename = T.getFilename() + "4";
        T.low_right = new QuadTree(T.getMidlat(), T.getMidlon(), T.getLrlat(), T.getLrlon(), filename);
    }

    public void initHelp(QuadTree T){
        InitLow_Left(T);
        InitLow_right(T);
        InitUp_Left(T);
        InitUp_Right(T);
    }

    public void init(QuadTree T){
        if (T.getDepth() == MaxDepth)
            return;
        if (T.getDepth() < MaxDepth){
            initHelp(T);
            for (QuadTree i : T){
                init(i);
            }
        }
    }
    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */
    public Rasterer(String imgRoot) {
        root = new QuadTree(MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON, MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON, "root");
        init(root);
        this.imgroot = imgRoot;
    }

    public boolean intersectsTile(QuadTree T){
        if (lrlat_of_query >= T.ullat)
            return false;
        if (lrlon_of_query <= T.ullon)
            return false;
        if (ullat_of_query <= T.lrlat)
            return false;
        if (ullon_of_query >= T.lrlon)
            return false;
        return true;
    }

    public boolean islonDPPTooBig(QuadTree T){
        if (T.depth < DepthExpect)
            return true;
        return false;
    }

    public boolean islonDPPEqual(QuadTree T){
        if (T.depth == DepthExpect)
            return true;
        return false;
    }

    public void intersect(QuadTree T, ArrayList<QuadTree> list){
        if (!intersectsTile(T))
            return;
        else{
            if (islonDPPEqual(T)) {
                list.add(T);
                return;
            }
            if (islonDPPTooBig(T)){
                for (QuadTree i : T)
                    intersect(i, list);
            }
        }
    }
    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     * </p>
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     *                    Can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     *                    forget to set this to true! <br>
     * @see #REQUIRED_RASTER_REQUEST_PARAMS
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        w = params.get("w");
        h = params.get("h");
        DepthExpect = -1;
        ullat_of_query = params.get("ullat");
        lrlat_of_query = params.get("lrlat");
        lrlon_of_query = params.get("lrlon");
        ullon_of_query = params.get("ullon");
        Map<String, Object> result = new HashMap<>();
        if ((ullat_of_query <= lrlat_of_query) || (ullon_of_query >= lrlon_of_query)){
            result.put("query_success", false);
            return result;
        }
        double distance_per_pixel_longitude_of_parms = (lrlon_of_query - ullon_of_query)/(w);
        for (int i = 0; i < distance_per_pixel_longitude_per_depth.length; i++){
            if (distance_per_pixel_longitude_per_depth[i] <= distance_per_pixel_longitude_of_parms) {
                DepthExpect = i;
                break;
            }
        }
        if (DepthExpect == -1)
            DepthExpect = 7;
        ArrayList<QuadTree> list = new ArrayList<>();
        intersect(root, list);
        if (list.isEmpty()){
            result.put("query_success", false);
            return result;
        }
        else{
            String[][] files;
            int length = 0;
            list.sort(QuadTree::compareTo);
            for (int i = 0; i < list.size(); i++){
                if (i + 1 == list.size()){
                    length = i + 1;
                    break;
                }
                if (list.get(i).ullat != list.get(i + 1).ullat){
                    length = i + 1;
                    break;
                }
            }
            int row = list.size() / length;
            files = new String[row][length];
            for (int i = 0; i < row; i++)
                for (int j = 0; j < length; j++){
                    files[i][j] = imgroot + list.get(i * length + j).filename + ".png";
                }
            result.put("render_grid", files);
            result.put("raster_ul_lon", list.get(0).ullon);
            result.put("raster_ul_lat", list.get(0).ullat);
            result.put("raster_lr_lon", list.get(list.size() - 1).lrlon);
            result.put("raster_lr_lat", list.get(list.size() - 1).lrlat);
            result.put("depth", list.get(0).depth);
            result.put("query_success", true);
        }
        return result;
    }

}
