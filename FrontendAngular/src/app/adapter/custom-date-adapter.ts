// custom-date-adapter.ts
import {Injectable} from '@angular/core';
import {NativeDateAdapter} from '@angular/material/core';

@Injectable()
export class PolishDateAdapter extends NativeDateAdapter {
  override getMonthNames(style: 'long' | 'short' | 'narrow'): string[] {
    const long = [
      'styczeń', 'luty', 'marzec', 'kwiecień',
      'maj', 'czerwiec', 'lipiec', 'sierpień',
      'wrzesień', 'październik', 'listopad', 'grudzień'
    ];
    const short = [
      'sty', 'lut', 'mar', 'kwi',
      'maj', 'cze', 'lip', 'sie',
      'wrz', 'paź', 'lis', 'gru'
    ];
    const narrow = [
      'S', 'L', 'M', 'K',
      'M', 'C', 'L', 'S',
      'W', 'P', 'L', 'G'
    ];

    switch (style) {
      case 'long':
        return long;
      case 'short':
        return short;
      case 'narrow':
        return narrow;
    }
  }

  override getDayOfWeekNames(style: 'long' | 'short' | 'narrow'): string[] {
    const long = [
      'niedziela', 'poniedziałek', 'wtorek', 'środa',
      'czwartek', 'piątek', 'sobota'
    ];
    const short = [
      'ndz', 'pon', 'wt', 'śr',
      'czw', 'pt', 'sob'
    ];
    const narrow = [
      'N', 'P', 'W', 'Ś',
      'C', 'P', 'S'
    ];

    switch (style) {
      case 'long':
        return long;
      case 'short':
        return short;
      case 'narrow':
        return narrow;
    }
  }

  override getFirstDayOfWeek(): number {
    return 1;
  }
}
