                SPOTIFY_SEARCH_API="https://api.spotify.com/v1/search";

                SPOTIFY_TOKEN_URI="https://accounts.spotify.com/api/token";
                SHPOTIFY_CREDENTIALS=$(printf "3c75fba9c71245cd83d680d26b2a0498:a976921c1fc3410f95e31b1e950b59a4" | base64 | tr -d "\n"|tr -d '\r');
                SPOTIFY_PLAY_URI="";

                type="track"
                Q="$1"

                   cecho "Connecting to Spotify's API";

                    SPOTIFY_TOKEN_RESPONSE_DATA=$( \
                        curl "${SPOTIFY_TOKEN_URI}" \
                            --silent \
                            -X "POST" \
                            -H "Authorization: Basic ${SHPOTIFY_CREDENTIALS}" \
                            -d "grant_type=client_credentials" \
                    )
                    if ! [[ "${SPOTIFY_TOKEN_RESPONSE_DATA}" =~ "access_token" ]]; then
                        cecho "Autorization failed, please check ${USER_CONFG_FILE}"
                        cecho "${SPOTIFY_TOKEN_RESPONSE_DATA}"
                        showAPIHelp
                        exit 1
                    fi
                    SPOTIFY_ACCESS_TOKEN=$( \
                        printf "${SPOTIFY_TOKEN_RESPONSE_DATA}" \
                        | command grep -E -o '"access_token":".*",' \
                        | sed 's/"access_token"://g' \
                        | sed 's/"//g' \
                        | sed 's/,.*//g' \
                    )

                    cecho "Searching ${type}s for: $Q";

                    SPOTIFY_PLAY_URI=$( \
                        curl -s -G $SPOTIFY_SEARCH_API \
                            -H "Authorization: Bearer ${SPOTIFY_ACCESS_TOKEN}" \
                            -H "Accept: application/json" \
                            --data-urlencode "q=$Q" \
                            -d "type=$type&limit=5&offset=0" \
                    )
                    echo "${SPOTIFY_PLAY_URI}"
                
                    
                

            
