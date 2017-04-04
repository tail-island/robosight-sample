(ns robosight.sample
  (:require   (clojure.data [json :as json]))
  (:gen-class :name com.tail_island.robosight.Sample
              :main true))

(defn -main
  [& args]
  (doseq [objects-string (take-while identity (repeatedly read-line))]
    (binding [*out* *err*]
      (println (json/read-str objects-string :key-fn keyword)))
    (->> (repeatedly #(let [r (rand)]
                        (cond
                          (< r 0.4) {:function "forward"  :parameters [(rand)]}
                          (< r 0.8) {:function "turn-to"  :parameters [(* Math/PI 2 (rand))]}
                          (< r 0.9) {:function "shoot"    :parameters [(+ (* (rand) 5) 5)]}
                          :else     nil)))
         (take 5)
         (json/write-str)
         (println))))
