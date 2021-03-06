(ns p-p-p-pokerface)

(defn rank [card]
  (let [ranks {\T 10, \J 11, \Q 12, \K 13, \A 14}
	fst (fn [[x _]] x)
	char (fst card)]
    (if (Character/isDigit char) (Integer/valueOf (str char)) (get ranks char))))

(defn suit [card]
  (let [snd (fn [[_ x]] (str x))]
	(snd card)))

(defn pair? [hand]
  (let [ranks (map rank hand)
        freq (frequencies ranks)
	values (vals freq)
	max-val (apply max values)]
   (< 1 max-val)))

(defn three-of-a-kind? [hand]
   (let [ranks (map rank hand)
        freq (frequencies ranks)
	values (vals freq)
	max-val (apply max values)]
   (< 2 max-val)))

(defn four-of-a-kind? [hand]
  (let [ranks (map rank hand)
        freq (frequencies ranks)
	values (vals freq)
	max-val (apply max values)]
   (< 3 max-val)))


(defn flush? [hand]
    (let [suite (map suit hand)
        freq (frequencies suite)
	values (vals freq)
	max-val (apply max values)]
   (== 5 max-val)))

(defn full-house? [hand]
   (let [ranks (map rank hand)
        freq (frequencies ranks)
	values (sort (vals freq))]
   (= [2 3] values)))

(defn two-pairs? [hand]
    (let [ranks (map rank hand)
        freq (frequencies ranks)
	values (sort (vals freq))]
   (= [1 2 2] values)))

(defn straight? [hand]
   (let [ranks (sort (map rank hand))
	alt-ranks (sort (replace {14 1} ranks))
        smallest (first ranks)
	smallest-alt (first alt-ranks)]
   (or (= ranks (range smallest (+ smallest 5))) 
       (= alt-ranks (range smallest-alt (+ smallest-alt 5))))))

(defn straight-flush? [hand]
  (and (straight? hand) (flush? hand)))

(defn high-card? [hand]
  true)

(defn value [hand]
  (let [checkers #{[high-card? 0]  [pair? 1]
                 [two-pairs? 2]  [three-of-a-kind? 3]
                 [straight? 4]   [flush? 5]
                 [full-house? 6] [four-of-a-kind? 7]
                 [straight-flush? 8]}
	has-value? (fn [check-box] ((first check-box) hand))
	success-checks (filter has-value? checkers)
	values (map second success-checks)]
	(apply max values)))

