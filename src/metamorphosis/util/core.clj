(ns metamorphosis.util.core)

(def theorem-log "resources/theorem.log")
(def results-counter "resources/record-result.txt")
(def results-root "resources/results")

(defn handle-timer 
    "A function that updates a time map with a counter"
    [state]
    (let [time (:time state)
          inc-time (inc (:count time))
          done? (>= inc-time (:target time))
          new-time (assoc time 
                          :finished? done? 
                          :count (if done? 0 inc-time))]
        (assoc state :time new-time)))

(defn reset-time
    "A function that updates the state by resetting the count and finished value of the time map"
    [state]
    (let [time (:time state)]
        (assoc state
            :time (assoc time
                    :count 0
                    :finished? false))))

(defn time-up?
    "A function that returns true or false if the time finished or not"
    [state]
    (let [time (:time state)]
        (:finished? time)))

(defn get-result-counter 
    "A function that gets the current result counter from the result recorder"
    []
    (Integer/parseInt (slurp results-counter)))

(defn update-result-log
    "A function that checks the record-result file and increments the number to keep track of the version"
    []
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

(defn update-util
    "A wrapper function that calls all utility functions"
    [state]
    (handle-timer state))