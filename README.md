# Android Core
## Setup
  - Import the `google-services.json` file into your `app/` project folder.
  - Place your `keystore.jks` file in the `/config` directory.
  - Create a `keystore.properties` file in the `/config` directory with the following content:
    ```properties
    storePassword=your_store_password
    keyAlias=your_key_alias
    keyPassword=your_key_password
    storeFile=../config/keystore.jks
    ```
  - Update Firebase App ID `your_firebase_app_id` in `fastlane/Fastfile`:
     ```ruby
    firebase_app_distribution(
      app: "your_firebase_app_id",
      testers_file: "./testers.txt",
      release_notes_file: "./release_notes.txt"
    )
    ```
