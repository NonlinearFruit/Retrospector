using System.Collections.Generic;
using System.Linq;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Retrospector.DataStorage;
using Retrospector.Search.Interfaces;

namespace Retrospector.Search
{
    public class SearchDataGateway : ISearchDataGateway
    {
        private readonly IMediaReducer _mediaReducer;
        private readonly IReviewReducer _reviewReducer;
        private readonly IFactoidReducer _factoidReducer;
        private readonly IDatabaseContext _context;
        private readonly ISearchFilterBuilder _filterBuilder;

        public SearchDataGateway(IMediaReducer mediaReducer,
            IReviewReducer reviewReducer,
            IFactoidReducer factoidReducer,
            IDatabaseContext context,
            ISearchFilterBuilder filterBuilder)
        {
            _mediaReducer = mediaReducer;
            _reviewReducer = reviewReducer;
            _factoidReducer = factoidReducer;
            _context = context;
            _filterBuilder = filterBuilder;
        }

        public IEnumerable<Dictionary<RetrospectorAttribute, string>> Search(QueryTree query)
        {
            if (query == null)
                return new Dictionary<RetrospectorAttribute, string>[0];
            var filter = _filterBuilder.BuildFilter(query);
            return (
                from media in _context.Media.AsEnumerable()
                join review in _context.Reviews on media.Id equals review.MediaId into mediaReviews
                from mediaReview in mediaReviews.AsEnumerable().DefaultIfEmpty()
                join factoid in _context.Factoids on media.Id equals factoid.MediaId into mediaFactoids
                from mediaFactoid in mediaFactoids.AsEnumerable().DefaultIfEmpty()
                where filter(media, mediaReview, mediaFactoid)
                select _mediaReducer
                    .Reduce(media)
                    .Concat(mediaReview != null ? _reviewReducer.Reduce(mediaReview) : new Dictionary<RetrospectorAttribute, string>())
                    .Concat(mediaFactoid != null ? _factoidReducer.Reduce(mediaFactoid) : new Dictionary<RetrospectorAttribute, string>())
                    .ToDictionary(x=>x.Key, x=>x.Value)
            ).ToList();
        }
    }
}