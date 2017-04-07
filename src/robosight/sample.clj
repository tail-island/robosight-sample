(ns robosight.sample
  (:require   (clojure.core [matrix :as matrix])
              (clojure.data [json   :as json]))
  (:gen-class :name com.tail_island.robosight.Sample
              :main true))

(def epsilon
  (Math/pow 10 -8))

(defn- angle
  [[x y]]
  (Math/atan2 y x))

(defn- normalize-angle
  [angle]
  (robosight.sample/angle [(Math/cos angle) (Math/sin angle)]))

(defn- anti-angle
  [angle]
  (normalize-angle (+ angle Math/PI)))

(defn -main
  [& args]
  ((fn [turn]
     (when-let [state-string (read-line)]
       (let [[friends enemies] (json/read-str state-string :key-fn keyword)]
         (->> friends
              (map (fn [friend]
                     (case (mod turn 11)
                       0 {:function "turn-to" :parameter (angle (matrix/sub (:center (->> enemies
                                                                                          (filter #(> (:hp %) 0.0))
                                                                                          (sort-by :hp)
                                                                                          (first)))
                                                                            (:center friend)))}
                       1 {:function "shoot"   :parameter (+ (* (rand) 5.0) 5.0)}
                       2 {:function "turn-to" :parameter (+ (anti-angle (angle (:center friend))) (- (rand Math/PI) (/ Math/PI 2)))}
                       3 {:function "forward" :parameter 1.0}
                       4 {:function "forward" :parameter 1.0}
                       5 {:function "forward" :parameter 1.0}
                       (let [anti-velocity-angle (anti-angle (angle (:velocity friend)))]
                         (if (> (Math/abs (- (:direction friend) anti-velocity-angle)) epsilon)
                           {:function "turn-to" :parameter anti-velocity-angle}
                           {:function "forward" :parameter (matrix/length (:velocity friend))})))))
              (json/write-str)
              (println)))
       (recur (inc turn))))
   0))
