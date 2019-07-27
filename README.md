# MoneyView

GoMoney EditText and TextView for formatting monetary values. Based on
[EasyMoney-Widgets](http://github.com/wajahatkarim3/EasyMoney-Widgets)

![alt tag](https://media.giphy.com/media/H1jtl3mCuJaEA4R5mT/giphy.gif)

## Installation

 Add the dependency : 
```kotlin
dependencies {
    implementation 'com.random-guys:moneyview:1.1.0'
}
```

## Usage

### XML : 
MoneyEditText
```xml
    <com.random_guys.moneyview.MoneyEditText
            android:id="@+id/moneyEditText"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/hint"
            android:background="@null"
            android:padding="16dp"
            android:inputType="numberDecimal"
            app:currency_symbol="@string/naira"/>
```
MoneyTextView
```xml
    <com.random_guys.moneyview.MoneyTextView
            android:id="@+id/moneyTextView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:textSize="30sp"
            app:currency_symbol="@string/naira"
            android:padding="16dp"
            android:textColor="@android:color/black"/>
```

### Kotlin Usage
Set the of MoneyTextView 
```kotlin
    moneyTextView.setMoneyText(String.format("%,.2f", ((System.currentTimeMillis() % 100000) / 3.0f)))
```

Set the value of MoneyEditText 
```kotlin
  moneyEditText.setText(String.format("%,.2f", ((System.currentTimeMillis() % 100000) / 3.0f)))
```

```
Copyright (c) 2019 Raymond Tukpe

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```