(ns robosight.sample
  (:require   (clojure.data [json :as json]))
  (:gen-class :name com.tail_island.robosight.Sample
              :main true))

(defn -main
  [& args]
  (doseq [objects-string (take-while identity (repeatedly read-line))]
    (->> (repeatedly #(let [r (rand)]
                        (cond
                          (< r 0.4) {:function "forward" :parameter (+ (* (rand) 0.5) 0.5)}
                          (< r 0.8) {:function "turn-to" :parameter (* Math/PI 2 (rand))}
                          (< r 0.9) {:function "shoot"   :parameter (+ (* (rand) 5.0) 5.0)}
                          :else     nil)))
         (take 5)
         (json/write-str)
         (println))))
