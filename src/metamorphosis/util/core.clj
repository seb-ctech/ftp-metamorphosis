(ns metamorphosis.util.core)

(defn handle-timer [state]
    (let [time (:time state)
          inc-time (inc (:count time))
          done? (>= inc-time (:target time))
          new-time (assoc (assoc time :finished? done?) :count (if done? 0 inc-time))]
        (when done? (println "next!"))
        (assoc state :time new-time)))

(defn update-util [state]
    (handle-timer state))

(defn time-up? [state]
    (let [time (:time state)]
        (:finished? time)))