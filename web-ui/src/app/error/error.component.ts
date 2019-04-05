import {Component, OnInit} from '@angular/core';
import {ErrorService} from './error.service';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html'
})
export class ErrorComponent implements OnInit {
  private errorMessage: string;
  private cuteImage: string;
  private cutePuppies = [
    'https://i.pinimg.com/originals/34/fc/5e/34fc5e5f5182d8ef77a0a3340a2dd6f6.jpg',
    'https://cdn.pixilart.com/photos/large/a8f368411291547.jpg',
    'http://en.bcdn.biz/Images/2016/11/15/776342f0-86f5-4522-84c9-a02d6b11c766.jpg',
    'https://dogsnet.com/wp-content/uploads/2018/09/cute-puppy-names.jpg',
    'https://images.mentalfloss.com/sites/default/files/styles/mf_image_16x9/public/istock-598825938.png?itok=yAcHEsp2&resize=1100x1100',
    'https://mauriziolacava.com/wp-content/uploads/2018/09/cute-puppy-pictures-science-why-adorable-puppies.jpeg',
    'https://cdn1-www.dogtime.com/assets/uploads/gallery/30-impossibly-cute-puppies/impossibly-cute-puppy-8.jpg',
    'https://static.makeuseof.com/wp-content/uploads/2017/11/puppy-photos-videos-670x335.jpg',
    'https://i.ytimg.com/vi/3ggIHfwkIWM/maxresdefault.jpg',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJ6Wnp7ws-n1Y-NUxtraZH5xmp4N0EuDjDJ1VafoF0t0ZSIwqs'
  ];
  constructor(private errorService: ErrorService, private router: Router) {}
  ngOnInit(): void {
    const error = this.errorService.getLatest();
    this.cuteImage = this.getCuteImage();
    if (!error) {
      this.router.navigateByUrl('/tv-show');
    } else if (error instanceof HttpErrorResponse) {
      this.errorMessage = error.error.message || error.message;
    } else {
      this.errorMessage = error.message;
    }
  }
  getCuteImage() {
    return this.cutePuppies[Math.floor(Math.random() * this.cutePuppies.length)];
  }
}
