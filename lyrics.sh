brew install khanhas/tap/spicetify-cli

spicetify

spicetify backup apply enable-devtool

mkdir ~/spicetify_data

mkdir ~/spicetify_data/CustomApps

cd "$(dirname "$(spicetify -c)")/CustomApps"

git clone https://github.com/khanhas/genius-spicetify genius

spicetify config custom_apps genius

spicetify apply