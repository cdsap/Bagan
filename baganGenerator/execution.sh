for i in `seq 1 2`; do ./gradlew clean :Tinder:assembleInternal -Ptinder.trackinglabel=bootstraping; done;
sed '$s/}/  fun myNewFunction0() {println(1) } }/' Tinder/src/main/java/com/tinder/analytics/adapter/LeanplumFireworksEventAdapter.kt > temp.kt && mv temp.kt Tinder/src/main/java/com/tinder/analytics/adapter/LeanplumFireworksEventAdapter.kt && rm temp.kt
for i in `seq 1 2`; do ./gradlew  :Tinder:assembleInternal -Ptinder.trackinglabel=Tinder; done;
for i in `seq 1 2`; do ./gradlew clean :Tinder:assembleInternal -Ptinder.trackinglabel=bootstraping; done;
sed '$s/}/  fun myNewFunction1() {println(2) } }/' data/src/main/java/com/tinder/data/mapper/MessageRequestBodyMapper.kt > temp.kt && mv temp.kt data/src/main/java/com/tinder/data/mapper/MessageRequestBodyMapper.kt && rm temp.kt
for i in `seq 1 2`; do ./gradlew  :Tinder:assembleInternal -Ptinder.trackinglabel=data; done;





