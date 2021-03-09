(ns metamorphosis.util.core)

(def theorem-log "resources/theorem.log")
(def results-counter "resources/record-result.txt")
(def results-root "resources/results")

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

(def number-length 4)
(defn number-padding 
    "A function that given a number compares it with the target length and pads the number"
    [n]
    (let [padding (- number-length (count (str n)))]
        (str (apply str (repeat padding 0)) (str n))))

(defn save-theorem
    "A function that stores the current printed theorem in the current results folder"
    [state]
    (when (:theorem state)
          (let [result (number-padding (get-result-counter))]
          (spit (str results-root "/" result "/" "theorem.log") (slurp theorem-log)))))