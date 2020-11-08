(ns aiezue.subtitle-corpus.wordcloud
  (:import (java.awt Dimension Color)
           (com.kennycason.kumo WordCloud CollisionMode)
           (com.kennycason.kumo.bg CircleBackground)
           (com.kennycason.kumo.palette LinearGradientColorPalette)
           (com.kennycason.kumo.font.scale SqrtFontScalar)
           (com.kennycason.kumo.nlp FrequencyAnalyzer))
  (:gen-class))

(defn wc
  "function for word cloud"
  [& _]
  (let [fq (-> (FrequencyAnalyzer.) (.load "subtitles_corpus.dat"))
        d (Dimension. 500 500)
        sqfs(SqrtFontScalar. 20 95)
        lgcp (LinearGradientColorPalette. Color/RED Color/BLUE Color/GREEN 40 40)
        cbg (CircleBackground. 300)
        wc (WordCloud. d CollisionMode/PIXEL_PERFECT)]
    (doto wc
      (.setPadding 2)
      (.setColorPalette lgcp)
      (.setBackground cbg)
      (.setFontScalar sqfs)
      (.build fq)
      (.writeToFile "subtitles_corpus.png"))))

(defn -main
  "paint it on the walls"
  [& _]
  (wc))
