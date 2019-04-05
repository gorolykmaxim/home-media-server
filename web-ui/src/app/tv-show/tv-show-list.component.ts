import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {MatBottomSheet} from '@angular/material';
import {AddTvShowPopupComponent} from './add-tv-show-popup.component';
import {TvShowService} from './tv-show.service';
import {ErrorService} from '../error/error.service';

@Component({
  selector: 'app-tv-show',
  templateUrl: './tv-show-list.component.html'
})
export class TvShowListComponent implements OnInit, OnDestroy {
  private tvShows: any[];
  private thumbnails: string[];
  constructor(private router: Router, private tvShowService: TvShowService, private bottomSheet: MatBottomSheet,
              private errorService: ErrorService) {}
  addShow() {
    this.bottomSheet.open(AddTvShowPopupComponent);
  }
  openShow(index) {
    const tvShow = this.tvShows[index];
    this.router.navigateByUrl(`/tv-show/${tvShow.id}`);
  }
  async ngOnInit(): Promise<void> {
    this.errorService.registerFailingComponent(TvShowListComponent);
    this.tvShowService.getTvShowsObservable().subscribe(tvShows => {
      this.tvShows = tvShows;
      this.thumbnails = this.tvShows.map(tvShow => tvShow.thumbnail);
    });
    try {
      await this.tvShowService.refreshTvShows();
    } catch (e) {
      this.bottomSheet.dismiss();
      this.errorService.notifyAbout(e, TvShowListComponent);
    }
  }
  ngOnDestroy(): void {
    this.errorService.unregisterFailingComponent(TvShowListComponent);
  }
}
