(ns metamorphosis.util.core)

(defn handle-timer [state]
    (let [time (:time state)
          inc-time (inc (:count time))
          done? (>= inc-time (:target time))
          new-time (assoc time 
                          :finished? done? 
                          :count (if done? 0 inc-time))]
        (assoc state :time new-time)))

(defn reset-time [state]
    (let [time (:time state)]
        (assoc state
            :time (assoc time
                    :count 0
                    :finished? false))))

(defn update-util [state]
    (handle-timer state))

(defn time-up? [state]
    (let [time (:time state)]
        (:finished? time)))