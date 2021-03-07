using Retrospector.Core.Crud.Models;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Boundary
{
    public class RequestRouter : IRequestRouter
    {
        private readonly IUseCase<CrudRequest<Media>> _mediaUse;
        private readonly IUseCase<CrudRequest<Review>> _reviewUse;
        private readonly IUseCase<CrudRequest<Factoid>> _factoidUse;
        private readonly IUseCase<SearchRequest> _searchUse;

        public RequestRouter(IUseCase<CrudRequest<Media>> mediaUse, IUseCase<CrudRequest<Review>> reviewUse, IUseCase<CrudRequest<Factoid>> factoidUse, IUseCase<SearchRequest> searchUse)
        {
            _mediaUse = mediaUse;
            _reviewUse = reviewUse;
            _factoidUse = factoidUse;
            _searchUse = searchUse;
        }

        public void Disseminate(IRequest request)
        {
            switch (request)
            {
                case CrudRequest<Media> mediaRequest:
                    _mediaUse.Execute(mediaRequest);
                    break;
                case CrudRequest<Review> reviewRequest:
                    _reviewUse.Execute(reviewRequest);
                    break;
                case CrudRequest<Factoid> factoidRequest:
                    _factoidUse.Execute(factoidRequest);
                    break;
                case SearchRequest searchRequest:
                    _searchUse.Execute(searchRequest);
                    break;
            }
        }
    }
}