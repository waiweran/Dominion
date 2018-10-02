cd adventures

find ./ -type f -exec sed -i -e 's/treasure += 2/addTreasure(2)/g' {} \;
rm *-e

cd ../alchemy

find ./ -type f -exec sed -i -e 's/treasure += 2/addTreasure(2)/g' {} \;
rm *-e

cd ../base

find ./ -type f -exec sed -i -e 's/treasure += 2/addTreasure(2)/g' {} \;
rm *-e

cd ../cornucopia

find ./ -type f -exec sed -i -e 's/treasure += 2/addTreasure(2)/g' {} \;
rm *-e

cd ../darkAges

find ./ -type f -exec sed -i -e 's/treasure += 2/addTreasure(2)/g' {} \;
rm *-e

cd ../extra

find ./ -type f -exec sed -i -e 's/treasure += 2/addTreasure(2)/g' {} \;
rm *-e

cd ../hinterlands

find ./ -type f -exec sed -i -e 's/treasure += 2/addTreasure(2)/g' {} \;
rm *-e

cd ../intrigue

find ./ -type f -exec sed -i -e 's/treasure += 2/addTreasure(2)/g' {} \;
rm *-e

cd ../promo

find ./ -type f -exec sed -i -e 's/treasure += 2/addTreasure(2)/g' {} \;
rm *-e

cd ../prosperity

find ./ -type f -exec sed -i -e 's/treasure += 2/addTreasure(2)/g' {} \;
rm *-e

cd ../seaside

find ./ -type f -exec sed -i -e 's/treasure += 2/addTreasure(2)/g' {} \;
rm *-e

cd ..
