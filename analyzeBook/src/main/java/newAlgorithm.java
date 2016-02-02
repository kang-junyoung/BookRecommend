import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jykang on 15. 10. 15..
 */
public class newAlgorithm {
    public static void main(String []args){

        Integer [][]ItemArr = {{0,1,1,1},
                {1,0,1,1},
                {1,1,0,0},
                {1,1,0,0}};
        int numBook = 4;
        List<Integer> A = Arrays.asList(ItemArr[0]);
        ArrayList<Integer> A1 = new ArrayList<>(A);

        for(int i=1; i<numBook; i++)
        {
            List<Integer> B = Arrays.asList(ItemArr[i]);
            ArrayList<Integer> B1 = new ArrayList<>(B);
            double cosine = CosineSimilarity.calConsineSimilarity(A1,B1);
            System.out.println("1, " + (i+1) +" : " +cosine );
        }

    }
}