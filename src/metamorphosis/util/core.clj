(ns metamorphosis.util.core)

(def results-counter "resources/record-result.txt")

(defn handle-timer [state]
    "A function that updates a time map with a counter"
    (let [time (:time state)
          inc-time (inc (:count time))
          done? (>= inc-time (:target time))
          new-time (assoc time 
                          :finished? done? 
                          :count (if done? 0 inc-time))]
        (assoc state :time new-time)))

(defn reset-time [state]
    "A function that updates the state by resetting the count and finished value of the time map"
    (let [time (:time state)]
        (assoc state
            :time (assoc time
                    :count 0
                    :finished? false))))

(defn update-util [state]
    "A wrapper function that calls all utility functions"
    (handle-timer state))

(defn time-up? [state]
    "A function that returns true or false if the time finished or not"
    (let [time (:time state)]
        (:finished? time)))

(defn get-result-counter []
    (Integer/parseInt (slurp results-counter)))

(defn update-result-log []
    "A function that checks the record-result file and increments the number to keep track of the version"
    (if (.exists (clojure.java.io/file results-counter))
        (spit results-counter (str (inc (get-result-counter))))
        (spit results-counter (str 1))))