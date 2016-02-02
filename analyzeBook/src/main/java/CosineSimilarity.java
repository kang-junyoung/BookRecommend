import java.util.*;
/**
 * Created by jykang on 15. 2. 18..
 */
public class CosineSimilarity {
    public static double calConsineSimilarity(ArrayList<Integer> first, ArrayList<Integer>second) {
        Double similarity = 0.0;
        Double sum = 0.0;    // the numerator of the cosine similarity (분자)
        Double fnorm = 0.0; // the first part of the denominator of the cosine similarity
        Double snorm = 0.0; // the second part of the denominator of the cosine similarity

        for (int i = 0; i < first.size(); i++) {
            sum = sum + first.get(i) * second.get(i);
        }//시그마계산
        fnorm = calculateNorm(first);
        snorm = calculateNorm(second);

        similarity = sum / (fnorm*snorm);

        return similarity;
    }

    public static Double calculateNorm(ArrayList<Integer> vec){
        Double norm = 0.0;
        for(int i=0; i< vec.size(); i++){
            norm = norm + Math.pow(vec.get(i),2);
        }
        return Math.sqrt(norm);

    }

}
//        Set<String> fkeys = first.keySet();
//        Iterator<String> fit = fkeys.iterator();
//        while(fit.hasNext()){
//            String featurename = fit.next(); // featurename = key 값임
//            boolean containKey = second.containsKey(featurename);
//            if(containKey){
//                sum = sum + first.get(featurename)* second.get(featurename);
//            }
//        }
//        fnorm= calculateNorm(first);
//        snorm =calculateNorm(second);
//
//        similarity = sum / (fnorm * snorm);
//        return similarity;
//    }


//        Double norm=0.0;
//        Set<String> keys = feature.keySet();
//        Iterator<String> fits = keys.iterator();
//        while(fits.hasNext()){
//            String featurename = fits.next();
//            norm = norm + Math.pow(feature.get(featurename),2);
//        }
//        return Math.sqrt(norm);
//    }
//}
