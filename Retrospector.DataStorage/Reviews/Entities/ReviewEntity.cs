using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Retrospector.DataStorage.Medias.Entities;

namespace Retrospector.DataStorage.Reviews.Entities
{
    [Table("Reviews")]
    public class ReviewEntity
    {
        [Key]
        public int Id { get; set; }
        public string User { get; set; }
        public int Rating { get; set; }
        public DateTime Date { get; set; }
        public string Content { get; set; }
        public DateTime CreatedDate { get; set; }
        public int MediaId { get; set; }
        public virtual MediaEntity Media { get; set; }
    }
}